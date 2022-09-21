package services;

import DAO.Database;
import helper.CacheData;
import helper.PingUtil;
import helper.PollingUtil;
import model.DeviceModel;
import websocket.WebSocketServerClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class PollingService {

    public static void discovery(DeviceModel deviceModel) {

        Database db = new Database();

        HashMap<String, Object> rs = new HashMap<>();

        rs.put("type", "notification");

        rs.put("title","Discovery Result");

        try {

            PingUtil p;

            if (deviceModel.getType().equals("ping")) {

                if (PingUtil.isUp(deviceModel.getIp())) {

                    rs.put("status", deviceModel.getIp() + " :Discovery successful");

                    rs.put("code", Constants.SUCCESS);

                    db.databaseDMLOperation("update", "update tbl_devices set provision = 1 where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(deviceModel.getId()))));

                } else {

                    rs.put("status", deviceModel.getIp() + " :Discovery failed");

                    rs.put("code", Constants.ERROR);

                }

            } else {

                p = new PingUtil();

                if (PingUtil.isUp(deviceModel.getIp())) {

                    PollingUtil polling = new PollingUtil();

                    HashMap<String, String> data = db.databaseSelectOperation("select username,password from tbl_devices where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(deviceModel.getId())))).get(0);

                    HashMap<String, String> metric = polling.polling(data.get("username"), data.get("password"), deviceModel.getIp(), 22);

                    if (metric.get("code").equals(Constants.SUCCESS_STR)) {

                        rs.put("status", deviceModel.getIp() + " :Discovery successful");

                        rs.put("code", Constants.SUCCESS);

                        db.databaseDMLOperation("update", "update tbl_devices set provision = 1 where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(deviceModel.getId()))));

                    } else {

                        rs.put("status", metric.get("status"));

                        rs.put("code", Constants.ERROR);

                    }

                } else {

                    rs.put("status", deviceModel.getIp() + " :Discovery failed");

                    rs.put("code", Constants.ERROR);

                }

            }

            WebSocketServerClass.sendMessage(deviceModel.getSocketId(), rs);

        } catch (SQLException e) {

            rs.put("status", "Error occurred during discovery");

            rs.put("code", Constants.ERROR);

            WebSocketServerClass.sendMessage(deviceModel.getSocketId(), rs);

            e.printStackTrace();

        }
    }

    public static void putDeviceForMonitoring(DeviceModel deviceModel) {

        Database db = new Database();

        HashMap<String, Object> rs = deviceModel.getResult();

        rs.put("type", "notification");

        rs.put("title","Discovery Result");

        HashMap<String, String> data = null;

        try {

            data = db.databaseSelectOperation("select * from tbl_devices where id = ?", new ArrayList<>(Collections.singletonList(String.valueOf(deviceModel.getId())))).get(0);

            ArrayList<HashMap<String, String>> existingDevices = db.databaseSelectOperation("select * from tbl_monitor_devices where id = ?", new ArrayList<Object>(
                    Collections.singletonList(data.get("id"))));

            db.databaseDMLOperation("update", "update tbl_devices set provision = 2 where id = ?", new ArrayList<>(Collections.singletonList(String.valueOf(deviceModel.getId()))));

            if (existingDevices.size() > 0) {

                db.databaseDMLOperation("update", "update tbl_monitor_devices set ip = ? ,deviceName = ?,username = ? , password = ? where id = ?",
                        new ArrayList<>(Arrays.asList(data.get("ip"),data.get("deviceName"), data.get("username"), data.get("password"), data.get("id")))
                );

                rs.put("status", data.get("ip") + ": device updated for monitoring");

                rs.put("code", Constants.SUCCESS);

                CacheData.getData().put(data.get("ip"),Constants.UNKNOWN);

                return;
            }

            db.databaseDMLOperation("add", "insert into tbl_monitor_devices (id,deviceName,ip,type,username,password) values (?,?,?,?,?,?)",
                    new ArrayList<Object>(Arrays.asList(data.get("id"),data.get("deviceName"), data.get("ip"), data.get("type"), data.get("username"), data.get("password")))
            );

            rs.put("status", data.get("ip") + ": device added for monitoring");

            rs.put("code", Constants.SUCCESS);

            CacheData.getData().put(data.get("ip"),Constants.UNKNOWN);

        } catch (SQLException e) {

            rs.put("status", "Error occurred.");

            rs.put("code", Constants.ERROR);

            e.printStackTrace();
        }
    }
}
