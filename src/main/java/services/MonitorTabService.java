package services;

import DAO.Database;
import helper.CacheData;
import model.DeviceModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MonitorTabService {
    public static void getMonitorDevices(DeviceModel deviceModel) {

        Database db = new Database();

        HashMap<String, Object> rs = deviceModel.getResult();

        ArrayList<HashMap<String, String>> raw = null;

        try {

            raw = db.databaseSelectOperation("select * from tbl_monitor_devices", null);

            ArrayList<ArrayList<String>> output = new ArrayList<>();

            HashMap<String, Object> devicesStatus = new HashMap<>(CacheData.getData());

            String html, deviceStatus;

            for (HashMap<String, String> ele : raw) {

                html = "<button class='btn btn-outline-primary btn-sm viewMonitorButton' data-id='" + ele.get("id") + "' data-type='" + ele.get("type") + "'>VIEW</button><button class='btn btn-outline-danger btn-sm ms-2 deleteMonitorButton' data-id='" + ele.get("id") + "'>DELETE</button>";

                Object ip = devicesStatus.get(ele.get("ip"));

                if (Constants.UP.equals(ip)) {

                    deviceStatus = "<i class=\"bi text-success bi-arrow-up\"></i>";

                } else if (Constants.DOWN.equals(ip)) {

                    deviceStatus = "<i class=\"bi text-danger bi-arrow-down\"></i>";

                } else {

                    deviceStatus = "<i class=\"bi bi-arrow-repeat\"></i>";

                }

                output.add(new ArrayList<String>(Arrays.asList(

                        ele.get("deviceName"),

                        deviceStatus + " " + ele.get("ip"),

                        ele.get("type"),

                        html

                )));

            }

            rs.put("list", output);

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static void deleteMonitorDevice(DeviceModel deviceModel) {

        Database db = new Database();

        try {

            db.databaseDMLOperation("delete", "delete from metrics where device_id = ?", new ArrayList<Object>(Collections.singletonList(deviceModel.getId())));

            db.databaseDMLOperation("delete", "delete from tbl_monitor_devices where id = ?", new ArrayList<Object>(Collections.singletonList(deviceModel.getId())));

            HashMap<String, Object> rs = deviceModel.getResult();

            CacheData.excludeMonitorDevices();

            rs.put("status", "Device deleted");

            rs.put("code", Constants.ERROR);

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
