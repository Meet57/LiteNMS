package helper;

import com.jcraft.jsch.*;
import services.Constants;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PollingUtil {
    JSch jsch;
    Session session;
    Channel channel;

    public HashMap<String, String> polling(String username, String password,
                                           String host, int port) {

        if (!PingUtil.isUp(host)) return null;

        String commands = "free -m | grep 'Mem' | awk '{print $2,$3}' ; mpstat 1 3 | tail -n 1 | awk '{print $12}' ; df -h | grep \"/$\" | awk '{print $5}'";

        String output = sshConnection(username, password, host, port, commands);

        HashMap<String, String> metrics = new LinkedHashMap<>();

        try {

            if (output.split(" ")[0].equals("ERROR")) {

                metrics.put("code", Constants.ERROR_STR);

                metrics.put("status", output);

                return metrics;

            }

            String[] arr = output.split("\n");

            metrics.put("code", Constants.SUCCESS_STR);

            metrics.put("ip", host);

            metrics.put("mem", arr[0].split(" ")[0]);

            metrics.put("umem", arr[0].split(" ")[1]);

            metrics.put("cpu", arr[1]);

            metrics.put("disk", arr[2]);

        } catch (Exception e) {

            metrics.put("code", Constants.ERROR_STR);

            metrics.put("status", "Something went worng<br> Please check dependencies in the client monitor<br> Try installing sysstat in the system");

            return metrics;

        }

        return metrics;

    }

    public String sshConnection(String username, String password, String host, int port, String commands) {

        Session session = null;

        ChannelExec channel = null;

        String output = null;

        try {

            session = new JSch().getSession(username, host, port);

            session.setPassword(password);

            session.setConfig("StrictHostKeyChecking", "no");

            session.connect(20000);

            channel = (ChannelExec) session.openChannel("exec");

            channel.setCommand(commands);

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

            channel.setOutputStream(responseStream);

            channel.connect(10000);

            while (channel.isConnected()) {

                Thread.sleep(100);

            }

            output = responseStream.toString();

        } catch (JSchException e) {

            e.printStackTrace();

            return "ERROR " + e.getMessage();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } finally {

            if (session != null) {

                session.disconnect();

            }

            if (channel != null) {

                channel.disconnect();
            }
        }

        return output;

    }

//    public static void main(String[] args) {
//        System.out.println(new PollingUtil().polling("meet", ".,.", "10.20.40.224", 22));
//    }
}
