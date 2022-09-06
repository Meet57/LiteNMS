package helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.net.util.SubnetUtils;

public class Ping {
    ProcessBuilder processBuilder;
    Process process;
    BufferedReader in;

    public Ping() {
        this.processBuilder = new ProcessBuilder();
    }

    public Boolean isUp(String ipAddress){
        ArrayList<String> ip = new ArrayList<>();
        ip.add("fping");
        ip.add("-q");
        ip.add("-a");
        ip.add(ipAddress);

        return !processBuilderOutput(ip).isEmpty();
    }

    public void ping(ArrayList<String> ipAddresses) {
        ipAddresses.add(0,"fping");
        ipAddresses.add(1,"-q");
        ipAddresses.add(2,"-a");

        String output = processBuilderOutput(ipAddresses);

        System.out.println(Arrays.toString(output.split("\n")));
    }

    public String processBuilderOutput(ArrayList<String> commands){
        StringBuilder output = new StringBuilder();
        processBuilder.command(commands);
        try {
            process = processBuilder.start();
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s = null;
            while((s = in.readLine()) != null)
            {
                output.append(s);
                output.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(output);
    }

    public ArrayList<String> CIDRtoIP(String CIDR) {
        SubnetUtils subnetUtils = new SubnetUtils(CIDR);
        return new ArrayList<>(Arrays.asList(subnetUtils.getInfo().getAllAddresses()));
    }
}
