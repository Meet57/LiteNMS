package services;

import DAO.Database;
import helper.CacheData;
import helper.PingUtil;
import helper.polling.LiveCollector;
import helper.polling.MetricCollector;
import model.MetricModel;
import websocket.WebSocketServerClass;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MonitorDataService {

    public static void getMonitorData(MetricModel metricModel) {

        Database db = new Database();

        HashMap<String, Object> rs = metricModel.getResult();

        String startDate = String.valueOf(LocalDate.now()) + " 00:00:00";

        String endDate = String.valueOf(LocalDate.now()) + " 23:59:59";

        ArrayList<HashMap<String, String>> raw = null;

        try {

            raw = db.databaseSelectOperation(
                    "select * from metrics where device_id = ? and (timestamp between ? and ? )",
                    new ArrayList<>(Arrays.asList(metricModel.getDevice_id(), startDate, endDate))
            );

            if (raw.size() == 0) {

                rs.put("code", Constants.ERROR);

                rs.put("status", "Polling Data is not available yet");

                return;
            }

            float availability = Float.parseFloat(db.databaseSelectOperation(
                    "select round(100*SUM(status)/COUNT(*),2) as availability from metrics where device_id = ?  and (timestamp between ? and ? )",
                    new ArrayList<>(Arrays.asList(metricModel.getDevice_id(), startDate, endDate))
            ).get(0).get("availability"));

            int mem = 0, total_mem = 0;

            String disk = "";

            String ip = raw.get(raw.size() - 1).get("ip");

            ArrayList<Integer> packets = new ArrayList<>();

            ArrayList<Float> cpu = new ArrayList<>();

            ArrayList<String> time = new ArrayList<>();

            for (int i = 0; i < raw.size(); i++) {

                packets.add(4 - Integer.parseInt(raw.get(i).get("packet_loss")) / 25);

                time.add(raw.get(i).get("timestamp"));

                if (metricModel.getType().equals("ssh")) {

                    try {

                        cpu.add(Float.valueOf(raw.get(i).get("cpu")));

                        mem = Integer.parseInt(raw.get(i).get("mem"));

                        total_mem = Integer.parseInt(raw.get(i).get("total_mem"));

                        disk = raw.get(i).get("disk");

                    } catch (Exception e) {

                        cpu.add(0F);

                    }

                }

            }

            if (CacheData.getData().get(ip).equals(Constants.DOWN)) {

                rs.put("ip", "<div class='d-flex'><span class='text-muted mt-auto' id='lastPollTime'></span><h1>" + ip + "</h1><span class=\"badge h-100 mx-2 text-bg-danger\">DOWN</span>\n</div>");

            } else if ((CacheData.getData().get(ip).equals(Constants.UP))) {

                rs.put("ip", "<div class='d-flex'><span class='text-muted mt-auto' id='lastPollTime'></span><h1>" + ip + "</h1><span class=\"badge h-100 mx-2 text-bg-success\">UP</span>\n</div>");

            } else {

                rs.put("ip", "<div class='d-flex'><span class='text-muted mt-auto' id='lastPollTime'></span><h1>" + ip + "</h1><span class=\"badge h-100 mx-2 text-bg-dark\">UNKNOWN</span>\n</div>");

            }

            rs.put("code", Constants.SUCCESS);

            rs.put("availability", availability);

            rs.put("rtt", raw.get(raw.size() - 1).get("rtt"));

            rs.put("timestamp", raw.get(raw.size() - 1).get("timestamp").split(" ")[1]);

            rs.put("packets", packets);

            rs.put("mem", mem);

            rs.put("total_mem", total_mem);

            rs.put("disk", disk);

            rs.put("cpu", cpu);

            rs.put("time", time);

            rs.put("actions", "<button class='col-auto me-2 btn btn-outline-success pollNow' data-id='" + raw.get(raw.size() - 1).get("device_id") + "' >Poll Now</button><button class='col-auto btn btn-outline-primary pingNow' data-ip='" + ip + "' >Ping Now</button>");


        } catch (SQLException e) {

            rs.put("status", "Server Error");

            rs.put("code", Constants.ERROR);

            e.printStackTrace();

        }

    }

    public static void checkDeviceStatus(MetricModel metricModel) {

        HashMap<String, Object> rs = new HashMap<>();

        rs.put("title", "Monitor Result");

        rs.put("type", "notification");

        if (PingUtil.isUp(metricModel.getIp())) {

            rs.put("status", metricModel.getIp() + ": Device is up");

            rs.put("code", Constants.SUCCESS);

        } else {

            rs.put("status", metricModel.getIp() + ": Device is down");

            rs.put("code", Constants.ERROR);

        }

        WebSocketServerClass.sendMessage(metricModel.getSocketId(), rs);

    }

    public static void pollDevice(MetricModel metricModel) {

        HashMap<String, Object> rs = new HashMap<>();

        try {

            HashMap<String, String> device = new Database().databaseSelectOperation("select * from tbl_monitor_devices where id = ?", new ArrayList<>(Collections.singletonList(metricModel.getDevice_id()))).get(0);

            if (device.get("type").equals("ping")) {

                new Thread(LiveCollector.pingPolling(device.get("ip"), device.get("id"), metricModel.getSocketId())).start();

            } else {

                new Thread(LiveCollector.shhPolling(device.get("ip"), device.get("username"), device.get("password"), device.get("id"), metricModel.getSocketId())).start();

            }

        } catch (SQLException e) {

            rs.put("status", "Server Error");

            rs.put("code", Constants.ERROR);

            e.printStackTrace();

        }

    }

}
