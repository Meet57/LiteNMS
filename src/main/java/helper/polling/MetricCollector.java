package helper.polling;

import DAO.Database;
import helper.PingUtil;
import helper.PollingUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetricCollector {
    public static void startPolling() {
        System.out.println("Polling Started with :" + Runtime.getRuntime().availableProcessors() + " cores");
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ScheduledExecutorService scs = Executors.newSingleThreadScheduledExecutor();

        scs.scheduleAtFixedRate(
                schedulerTask(executorService),
                0,
                5,
                TimeUnit.MINUTES
        );
    }

    public static Runnable pingPolling(String[] ipAddresses) {
        return new Runnable() {
            @Override
            public void run() {
                String[] allResults = new PingUtil().ping(ipAddresses);
                for (String result : allResults) {

                    Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                    Matcher matcher = pattern.matcher(result);

                    matcher.find();

                    if (matcher.group(1).equals("0")) {

                        try {
                            new Database().DMLStatement(
                                    "add",
                                    "insert into metrics (ip, ts, type, packetloss, rtt) values (?,now(),'ping',?,?)",
                                    new ArrayList<Object>(Arrays.asList(result.split(" ")[0], matcher.group(1), matcher.group(3)))
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    } else {

                        try {
                            new Database().DMLStatement(
                                    "add",
                                    "insert into metrics (ip, ts, type, packetloss, rtt) values (?,now(),'ping',?,-1)",
                                    new ArrayList<Object>(Arrays.asList(result.split(" ")[0], matcher.group(1)))
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
        };
    }

    public static Runnable shhPolling(String ip, String username, String password) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    String pingRs = new PingUtil().ping(ip);

                    Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                    Matcher matcher = pattern.matcher(pingRs);

                    matcher.find();

                    if (matcher.group(1).equals("0")) {
                        PollingUtil poll = new PollingUtil();

                        HashMap<String, String> result = poll.polling(username, password, ip, 22);

                        result.put("rtt", matcher.group(3));
                        result.put("packetloss", matcher.group(1));

                        if (result.get("code").equals("1")) {
//                            ssh success
                            new Database().DMLStatement(
                                    "add",
                                    "insert into metrics (ip, ts, type, packetloss, rtt, cpu, mem, tmem, disk) values (?,now(),'ssh',?,?,?,?,?,?)",
                                    new ArrayList<Object>(Arrays.asList(result.get("ip"), result.get("packetloss"), result.get("rtt"), 100 - Float.parseFloat(result.get("cpu")), result.get("umem"), result.get("mem"), result.get("disk")))
                            );

                        } else {
//                            ssh fail
                            new Database().DMLStatement(
                                    "add",
                                    "insert into metrics (ip, ts, type, packetloss, rtt) values (?,now(),'ssh',?,?)",
                                    new ArrayList<Object>(Arrays.asList(ip, result.get("packetloss"), result.get("rtt")))
                            );
                        }

                    } else {

                        new Database().DMLStatement(
                                "add",
                                "insert into metrics (ip, ts, type, packetloss, rtt) values (?,now(),'ssh',?,-1)",
                                new ArrayList<Object>(Arrays.asList(ip, matcher.group(1)))
                        );

                    }

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        };
    }

    public static Runnable schedulerTask(ExecutorService executorService) {
        return new Runnable() {
            @Override
            public void run() {
                List<Runnable> runnables = new ArrayList<>();

                Database db = new Database();

                ArrayList<HashMap<String, String>> availableMonitor = null;
                try {
                    availableMonitor = db.select("select * from tbl_monitor_devices", null);

                    runnables.add(pingPolling(availableMonitor.stream().filter(device -> device.get("type").equals("ping")).map(device -> device.get("ip")).toArray(String[]::new)));

                    availableMonitor.stream().filter(device -> device.get("type").equals("ssh")).forEach(
                            monitor -> {
                                runnables.add(shhPolling(monitor.get("ip"), monitor.get("username"), monitor.get("password")));
                            }
                    );

                    for (int i = 0; i < runnables.size(); i++) {
                        executorService.submit(runnables .get(i));
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
