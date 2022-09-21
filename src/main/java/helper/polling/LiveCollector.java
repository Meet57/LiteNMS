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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiveCollector {

    public static Runnable pingPolling(String ipAddress, String id, String socketId) {
        return new Runnable() {
            @Override
            public void run() {
                String result = PingUtil.ping(ipAddress);

                Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                Matcher matcher = pattern.matcher(result);

                matcher.find();

                String ip = result.split(" ")[0];

                HashMap<String, Object> notification = new HashMap<>();

                notification.put("title", "Polling result for: " + ip);

                notification.put("id",id);

                notification.put("deviceType","ping");

                notification.put("type", "notification");

                try {

                    if (matcher.group(1).equals(Constants.ERROR_STR)) {

                        new Database().databaseDMLOperation(
                                "add",
                                "insert into metrics (ip, timestamp, type, packet_loss, rtt,status,device_id) values (?,now(),'ping',?,?,1,?)",
                                new ArrayList<Object>(Arrays.asList(ip, matcher.group(1), matcher.group(3), id))
                        );

                        notification.put("status", "Successful");

                        notification.put("code", 1);

                        CacheData.getData().put(result.split(" ")[0], Constants.UP);


                    } else {

                        new Database().databaseDMLOperation(
                                "add",
                                "insert into metrics (ip, timestamp, type, packet_loss, status,device_id) values (?,now(),'ping',?,0,?)",
                                new ArrayList<Object>(Arrays.asList(ip, matcher.group(1), id))
                        );

                        notification.put("status", "Failed: Device is DOWN");

                        notification.put("code", 0);

                        CacheData.getData().put(result.split(" ")[0], Constants.DOWN);

                    }

                } catch (SQLException e) {

                    notification.put("status", "Server Error");

                    notification.put("code", 0);

                    e.printStackTrace();

                } finally {

                    WebSocketServerClass.sendMessage(socketId, notification);

                }
            }
        };
    }

    public static Runnable shhPolling(String ip, String username, String password, String id, String socketId) {
        return new Runnable() {
            @Override
            public void run() {

                String pingRs = PingUtil.ping(ip);

                Pattern pattern = Pattern.compile(".* : xmt\\/rcv\\/%loss = \\d\\/\\d\\/(\\d+)%(, min\\/avg\\/max =.*\\/(\\d+.\\d+)\\/)*");

                Matcher matcher = pattern.matcher(pingRs);

                HashMap<String, Object> notification = new HashMap<>();

                notification.put("title", "Polling result for: " + ip);

                notification.put("type", "notification");

                notification.put("id",id);

                notification.put("deviceType","ssh");

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

                            notification.put("status", "Successful");

                            notification.put("code", 1);

                        } else {
//                            ssh fail
                            new Database().databaseDMLOperation(
                                    "add",
                                    "insert into metrics (ip, timestamp, type, packet_loss, rtt,status,device_id) values (?,now(),'ssh',?,?,1,?)",
                                    new ArrayList<Object>(Arrays.asList(ip, result.get("packet_loss"), result.get("rtt"), id))
                            );

                            notification.put("status", result.get("status"));

                            notification.put("code", 0);

                            CacheData.getData().put(ip, Constants.UP);
                        }

                    } else {

                        new Database().databaseDMLOperation(
                                "add",
                                "insert into metrics (ip, timestamp, type, packet_loss, status,device_id) values (?,now(),'ssh',?,0,?)",
                                new ArrayList<Object>(Arrays.asList(ip, matcher.group(1), id))
                        );

                        CacheData.getData().put(ip, Constants.DOWN);

                        notification.put("status", "Failed: Device is DOWN");

                        notification.put("code", 0);

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

                } finally {

                    WebSocketServerClass.sendMessage(socketId, notification);

                }
            }
        };
    }

}
