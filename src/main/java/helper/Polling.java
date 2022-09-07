package helper;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Polling {
    JSch jsch;
    Session session;
    Channel channel;

    public HashMap<String, String> polling(String username, String password,
                                           String host, int port) throws Exception {

        Ping ping = new Ping();
        if(!ping.isUp(host)) return null;

        ArrayList<String> commands = new ArrayList<>();

        commands.add("free -m | grep 'Mem' | awk '{print $2,$3}'");
        commands.add("mpstat 1 3 | tail -n 1 | awk '{print $12}'");
        commands.add("df -h | grep \"/$\" | awk '{print $5}'");

        String output = sshConnection(username, password, host, port, commands);

        HashMap<String,String> metrics = new LinkedHashMap<>();

        if(output.split(" ")[0].equals("ERROR")){
            metrics.put("code","0");
            metrics.put("status",output);
            return metrics;
        }

        String[] arr = output.split("\n");
        metrics.put("code","1");
        metrics.put("IP",host);
        metrics.put("Mem",arr[0].split(" ")[0]);
        metrics.put("UMem",arr[0].split(" ")[1]);
        metrics.put("ICPU",arr[1]);
        metrics.put("Storage",arr[2]);

        return metrics;
    }

    public String sshConnection(String username, String password, String host, int port, ArrayList<String> commands) {
        Session session = null;
        ChannelExec channel = null;
        StringBuilder output = new StringBuilder();

        try {
            session = new JSch().getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            for (String command : commands) {
                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);
                channel.connect();
                while (channel.isConnected()) {
                    Thread.sleep(100);
                }
                String responseString = responseStream.toString();
                output.append(responseString);
            }
        } catch (JSchException | InterruptedException e) {
            return "ERROR " + e.getMessage();
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return output.toString();
    }
}
