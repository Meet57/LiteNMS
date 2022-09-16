package helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PingUtil {

    public static Boolean isUp(String ipAddress) {

        ArrayList<String> ip = new ArrayList<>();

        ip.add("fping");

        ip.add("-q");

        ip.add("-a");

        ip.add(ipAddress);

        return !processBuilderOutput(ip).isEmpty();

    }

    public static String[] ping(String[] ipAddresses) {

        String ips = String.join(" ", ipAddresses);

        ArrayList<String> command = new ArrayList<>();

        command.add(0, "/bin/sh");

        command.add(1, "-c");

        command.add(2, "fping -q -c 4 " + ips);

        String output = processBuilderOutput(command);

        return output.split("\n");

    }

    public static String ping(String ipAddress) {

        ArrayList<String> command = new ArrayList<>();

        command.add(0, "/bin/sh");

        command.add(1, "-c");

        command.add(2, "fping -q -c 4 " + ipAddress);

        return processBuilderOutput(command);
    }

    public static String processBuilderOutput(ArrayList<String> commands) {

        ProcessBuilder processBuilder = new ProcessBuilder();

        Process process = null;

        BufferedReader in = null;

        StringBuilder output = new StringBuilder();

        try {

            processBuilder.command(commands);

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

            if (process != null) process.destroy();

            e.printStackTrace();

        }

        return String.valueOf(output);

    }

}
