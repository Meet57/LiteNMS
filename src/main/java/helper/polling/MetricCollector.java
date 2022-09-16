package helper.polling;

import DAO.Database;
import helper.CacheData;
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
                String[] allResults = PingUtil.ping(ipAddresses);

                for (String result : allResults) {

                    Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                    Matcher matcher = pattern.matcher(result);

                    matcher.find();

                    if (matcher.group(1).equals("0")) {

                        try {

                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, rtt,status) values (?,now(),'ping',?,?,1)",
                                    new ArrayList<Object>(Arrays.asList(result.split(" ")[0], matcher.group(1), matcher.group(3)))
                            );

                            CacheData.getData().put(result.split(" ")[0], "UP");

                        } catch (SQLException e) {

                            e.printStackTrace();

                        }

                    } else {

                        try {

                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, status) values (?,now(),'ping',?,0)",
                                    new ArrayList<Object>(Arrays.asList(result.split(" ")[0], matcher.group(1)))
                            );

                            CacheData.getData().put(result.split(" ")[0], "DOWN");

                        } catch (SQLException e) {

                            e.printStackTrace();

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


                String pingRs = PingUtil.ping(ip);

                Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                Matcher matcher = pattern.matcher(pingRs);

                try {

                    matcher.find();

                    if (matcher.group(1).equals("0")) {

                        PollingUtil poll = new PollingUtil();

                        HashMap<String, String> result = poll.polling(username, password, ip, 22);

                        result.put("rtt", matcher.group(3));

                        result.put("packet_loss", matcher.group(1));

                        if (result.get("code").equals("1")) {
//                            ssh success
                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, rtt, cpu, mem, total_mem, disk, status) values (?,now(),'ssh',?,?,?,?,?,?,1)",
                                    new ArrayList<Object>(Arrays.asList(result.get("ip"), result.get("packet_loss"), result.get("rtt"), 100 - Float.parseFloat(result.get("cpu")), result.get("umem"), result.get("mem"), result.get("disk")))
                            );

                            CacheData.getData().put(ip, "UP");

                        } else {
//                            ssh fail
                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, rtt,status) values (?,now(),'ssh',?,?,0)",
                                    new ArrayList<Object>(Arrays.asList(ip, result.get("packet_loss"), result.get("rtt")))
                            );

                            CacheData.getData().put(ip, "DOWN");
                        }

                    } else {

                        new Database().databaseDMLOperation(
                                "add",
                                "insert into metrics (ip, timestamp, type, packet_loss, status) values (?,now(),'ssh',?,0)",
                                new ArrayList<Object>(Arrays.asList(ip, matcher.group(1)))
                        );

                        CacheData.getData().put(ip, "DOWN");

                    }

                } catch (Exception e) {

                    try {

                        new Database().databaseDMLOperation(
                                "add",
                                "insert into metrics (ip, timestamp, type, packet_loss, status) values (?,now(),'ssh',?,0)",
                                new ArrayList<Object>(Arrays.asList(ip, matcher.group(1)))
                        );

                    } catch (SQLException ex) {

                        ex.printStackTrace();

                    }

                    CacheData.getData().put(ip, "DOWN");
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

                    availableMonitor = db.databaseSelectOperation("select * from tbl_monitor_devices", null);

                    runnables.add(pingPolling(availableMonitor.stream().filter(device -> device.get("type").equals("ping")).map(device -> device.get("ip")).toArray(String[]::new)));

                    availableMonitor.stream().filter(device -> device.get("type").equals("ssh")).forEach(
                            monitor -> {
                                runnables.add(shhPolling(monitor.get("ip"), monitor.get("username"), monitor.get("password")));
                            }
                    );

                    for (int i = 0; i < runnables.size(); i++) {

                        executorService.submit(runnables.get(i));

                    }

                } catch (SQLException e) {

                    e.printStackTrace();

                }
            }
        };
    }
}
