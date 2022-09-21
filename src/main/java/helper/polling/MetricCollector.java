package helper.polling;

import DAO.Database;
import helper.CacheData;
import helper.PingUtil;
import helper.PollingUtil;
import services.Constants;
import websocket.WebSocketServerClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetricCollector {
    public static void startPolling() {

        ExecutorService executorService = Executors.newFixedThreadPool(Constants.POLLING_THREADS);

        ScheduledExecutorService scs = Executors.newSingleThreadScheduledExecutor();

        scs.scheduleAtFixedRate(
                schedulerTask(executorService),
                0,
                5,
                TimeUnit.MINUTES
        );
    }

    public static Runnable pingPolling(HashMap<String, String> ipAddresses) {
        return new Runnable() {
            @Override
            public void run() {
                String[] allResults = PingUtil.ping(ipAddresses.keySet().toArray(new String[0]));

                Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                for (String result : allResults) {

                    Matcher matcher = pattern.matcher(result);

                    matcher.find();

                    String ip = result.split(" ")[0];

                    try {

                        if (matcher.group(1).equals(Constants.ERROR_STR)) {

                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, rtt,status,device_id) values (?,now(),'ping',?,?,1,?)",
                                    new ArrayList<Object>(Arrays.asList(ip, matcher.group(1), matcher.group(3), ipAddresses.get(ip)))
                            );

                            CacheData.getData().put(result.split(" ")[0], Constants.UP);


                        } else {

                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, status,device_id) values (?,now(),'ping',?,0,?)",
                                    new ArrayList<Object>(Arrays.asList(ip, matcher.group(1), ipAddresses.get(ip)))
                            );

                            CacheData.getData().put(result.split(" ")[0], Constants.DOWN);

                        }

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }
                }
            }
        };
    }

    public static Runnable shhPolling(String ip, String username, String password, String id) {
        return new Runnable() {
            @Override
            public void run() {

                String pingRs = PingUtil.ping(ip);

                Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                Matcher matcher = pattern.matcher(pingRs);

                try {

                    matcher.find();

                    if (matcher.group(1).equals(Constants.ERROR_STR)) {

                        PollingUtil poll = new PollingUtil();

                        HashMap<String, String> result = poll.polling(username, password, ip, 22);

                        result.put("rtt", matcher.group(3));

                        result.put("packet_loss", matcher.group(1));

                        if (result.get("code").equals(Constants.SUCCESS_STR)) {
//                            ssh success
                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, rtt, cpu, mem, total_mem, disk, status, device_id) values (?,now(),'ssh',?,?,?,?,?,?,1,?)",
                                    new ArrayList<Object>(Arrays.asList(result.get("ip"), result.get("packet_loss"), result.get("rtt"), 100 - Float.parseFloat(result.get("cpu")), result.get("umem"), result.get("mem"), result.get("disk"), id))
                            );

                            CacheData.getData().put(ip, Constants.UP);

                        } else {
//                            ssh fail
                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, rtt,status,device_id) values (?,now(),'ssh',?,?,1,?)",
                                    new ArrayList<Object>(Arrays.asList(ip, result.get("packet_loss"), result.get("rtt"), id))
                            );

                            result.put("title", "Polling Status for: " + ip);

                            result.put("type", "notification");

                            WebSocketServerClass.sendBroadcast(result);

                            CacheData.getData().put(ip, Constants.UP);
                        }

                    } else {

                        new Database().databaseDMLOperation(
                                "add",
                                "insert into metrics (ip, timestamp, type, packet_loss, status,device_id) values (?,now(),'ssh',?,0,?)",
                                new ArrayList<Object>(Arrays.asList(ip, matcher.group(1), id))
                        );

                        CacheData.getData().put(ip, Constants.DOWN);

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

                    CacheData.getData().put(ip, Constants.DOWN);
                    System.err.println(e.getMessage());
                }
            }
        };
    }

    public static Runnable schedulerTask(ExecutorService executorService) {
        return new Runnable() {
            @Override
            public void run() {

                Database db = new Database();

                ArrayList<HashMap<String, String>> availableMonitor = null;

                HashMap<String, String> rs = new HashMap<>();

                rs.put("type", "notification");

                rs.put("title", "Polling Status");

                try {

                    availableMonitor = db.databaseSelectOperation("select * from tbl_monitor_devices", null);

//                    executorService.submit(pingPolling(availableMonitor.stream().filter(device -> device.get("type").equals("ping")).map(device -> device.get("ip")).toArray(String[]::new)));

                    HashMap<String, String> pingDevices = new HashMap<>();

                    availableMonitor.stream().filter(device -> device.get("type").equals("ping")).forEach((device -> {
                        pingDevices.put(device.get("ip"), device.get("id"));
                    }));

                    executorService.submit(pingPolling(pingDevices));

                    availableMonitor.stream().filter(device -> device.get("type").equals("ssh")).forEach(
                            monitor -> {
                                executorService.submit(shhPolling(monitor.get("ip"), monitor.get("username"), monitor.get("password"), monitor.get("id")));
                            }
                    );

                    rs.put("status", "Polling started");

                    rs.put("code", Constants.SUCCESS_STR);

                } catch (SQLException e) {

                    e.printStackTrace();

                    rs.put("status", e.getMessage());

                    rs.put("code", Constants.ERROR_STR);

                } finally {

                    WebSocketServerClass.sendBroadcast(rs);

                }
            }
        };
    }
}
