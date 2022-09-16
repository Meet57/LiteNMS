package services;

import DAO.Database;
import helper.CacheData;
import helper.PingUtil;
import helper.PollingUtil;
import model.DeviceModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class PollingService {

    public static void discovery(DeviceModel deviceModel) {

        Database db = new Database();

        HashMap<String, Object> rs = deviceModel.getResult();

        try {

            PingUtil p;

            if (deviceModel.getType().equals("ping")) {

                if (PingUtil.isUp(deviceModel.getIp())) {

                    rs.put("status", deviceModel.getIp() + " :Discovery successful");

                    rs.put("code", 1);

                    db.databaseDMLOperation("update", "update tbl_devices set provision = 1 where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(deviceModel.getId()))));

                } else {

                    rs.put("status", deviceModel.getIp() + " :Discovery failed");

                    rs.put("code", 0);

                }

            } else {

                p = new PingUtil();

                if (PingUtil.isUp(deviceModel.getIp())) {

                    PollingUtil polling = new PollingUtil();

                    HashMap<String, String> data = db.databaseSelectOperation("select username,password from tbl_devices where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(deviceModel.getId())))).get(0);

                    HashMap<String, String> metric = polling.polling(data.get("username"), data.get("password"), deviceModel.getIp(), 22);

                    if (metric.get("code").equals("1")) {

                        rs.put("status", deviceModel.getIp() + " :Discovery successful");

                        rs.put("code", 1);

                        db.databaseDMLOperation("update", "update tbl_devices set provision = 1 where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(deviceModel.getId()))));

                    } else {

                        rs.put("status", metric.get("status"));

                        rs.put("code", 0);

                    }

                } else {

                    rs.put("status", deviceModel.getIp() + " :Discovery failed");

                    rs.put("code", 0);

                }

            }

        } catch (SQLException e) {

            rs.put("status", "Error occurred during discovery");

            rs.put("code", 0);

            e.printStackTrace();

        }
    }

    public static void putDeviceForMonitoring(DeviceModel deviceModel) {

        Database db = new Database();

        HashMap<String, Object> rs = deviceModel.getResult();

        HashMap<String, String> data = null;

        try {

            data = db.databaseSelectOperation("select * from tbl_devices where id = ?", new ArrayList<>(Collections.singletonList(String.valueOf(deviceModel.getId())))).get(0);

            ArrayList<HashMap<String, String>> existingDevices = db.databaseSelectOperation("select * from tbl_monitor_devices where type = ? and ip = ?", new ArrayList<Object>(
                    Arrays.asList(data.get("type"), data.get("ip"))));

            db.databaseDMLOperation("update", "update tbl_devices set provision = 2 where id = ?", new ArrayList<>(Collections.singletonList(String.valueOf(deviceModel.getId()))));

            if (existingDevices.size() > 0) {

                db.databaseDMLOperation("update", "update tbl_monitor_devices set deviceName = ?,username = ? , password = ? where id = ?",
                        new ArrayList<>(Arrays.asList(data.get("deviceName"), data.get("username"), data.get("password"), existingDevices.get(0).get("id")))
                );

                rs.put("status", data.get("ip") + ": device updated for monitoring");

                rs.put("code", 1);

                return;
            }

            db.databaseDMLOperation("add", "insert into tbl_monitor_devices (deviceName,ip,type,username,password) values (?,?,?,?,?)",
                    new ArrayList<Object>(Arrays.asList(data.get("deviceName"), data.get("ip"), data.get("type"), data.get("username"), data.get("password")))
            );

            rs.put("status", data.get("ip") + ": device added for monitoring");

            rs.put("code", 1);

            CacheData.getData().put(data.get("ip"),"UNKNOWN");

        } catch (SQLException e) {

            rs.put("status", "Error occurred.");

            rs.put("code", 0);

            e.printStackTrace();
        }
    }
}
