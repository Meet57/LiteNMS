package helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.net.util.SubnetUtils;

public class PingUtil {
    ProcessBuilder processBuilder = new ProcessBuilder();
    Process process;
    BufferedReader in;

    public Boolean isUp(String ipAddress) {
        ArrayList<String> ip = new ArrayList<>();
        ip.add("fping");
        ip.add("-q");
        ip.add("-a");
        ip.add(ipAddress);

        return !processBuilderOutput(ip).isEmpty();
    }

    public String[] ping(String[] ipAddresses) {

        String ips = String.join(" ", ipAddresses);

        ArrayList<String> command = new ArrayList<>();
        command.add(0, "/bin/sh");
        command.add(1, "-c");
        command.add(2, "fping -q -c 4 " + ips);

        String output = processBuilderOutput(command);

        return output.split("\n");
    }

    public String ping(String ipAddress) {

        ArrayList<String> command = new ArrayList<>();
        command.add(0, "/bin/sh");
        command.add(1, "-c");
        command.add(2, "fping -q -c 4 " + ipAddress);

        return processBuilderOutput(command);
    }

    public String processBuilderOutput(ArrayList<String> commands) {
        StringBuilder output = new StringBuilder();
        processBuilder.command(commands);
        try {
            process = processBuilder.start();
            process.waitFor();
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = null;
            while ((s = in.readLine()) != null) {
                output.append(s);
                output.append(System.getProperty("line.separator"));
            }
            in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((s = in.readLine()) != null) {
                output.append(s);
                output.append(System.getProperty("line.separator"));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(output);
    }

    public ArrayList<String> CIDRtoIP(String CIDR) {
        SubnetUtils subnetUtils = new SubnetUtils(CIDR);
        return new ArrayList<>(Arrays.asList(subnetUtils.getInfo().getAllAddresses()));
    }
}
