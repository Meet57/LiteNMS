package services;

import DAO.Database;
import model.DeviceModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MonitorDiscoveryService {

    public static void getSingleDiscoveryDevice(DeviceModel deviceModel) {

        Database db = new Database();

        ArrayList<HashMap<String, String>> raw = null;

        try {
            raw = db.databaseSelectOperation("select id,deviceName,username,ip,type from tbl_devices where id = ?", new ArrayList<Object>(Collections.singletonList(deviceModel.getId())));

            HashMap<String, Object> rs = deviceModel.getResult();

            rs.put("data", raw.get(0));

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static void getAllDiscoveryDevices(DeviceModel deviceModel) {

        Database db = new Database();

        ArrayList<HashMap<String, String>> raw = null;

        try {

            raw = db.databaseSelectOperation("select * from tbl_devices", null);

            ArrayList<ArrayList<String>> output = new ArrayList<>();

            raw.forEach(ele -> {

                String html = "<button class='btn btn-outline-success btn-sm editButton' data-ip='" + ele.get("ip") + "' data-id='" + ele.get("id") + "'>EDIT</button><button class='btn btn-outline-danger btn-sm ms-2 deleteButton' data-ip='" + ele.get("ip") + "' data-id='" + ele.get("id") + "'>DELETE</button><button class='btn btn-outline-primary btn-sm ms-2 runButton' data-id='" + ele.get("id") + "' data-type='" + ele.get("type") + "' data-ip='" + ele.get("ip") + "' >RUN</button>";

                if (Integer.parseInt(ele.get("provision")) >= 1) {

                    html += "<button class='btn btn-outline-success btn-sm ms-2 provisionButton' data-id='" + ele.get("id") + "'>PROVISION</button>";

                }
                output.add(new ArrayList<String>(Arrays.asList(
                        ele.get("deviceName"),
                        ele.get("ip"),
                        ele.get("type"),
                        html
                )));

            });

            deviceModel.getResult().put("list", output);

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public static void deleteDiscoveryDevice(DeviceModel deviceModel) {

        Database db = new Database();

        HashMap<String, Object> rs = deviceModel.getResult();

        try {
            db.databaseDMLOperation("delete", "delete from tbl_devices where id = ?", new ArrayList<Object>(Collections.singletonList(String.valueOf(deviceModel.getId()))));

            rs.put("status", "Device deleted");

            rs.put("code", Constants.ERROR);

        } catch (SQLException e) {

            rs.put("status", "Error occurred while deleting");

            rs.put("code", Constants.ERROR);

            e.printStackTrace();

        }
    }

    public static void addDiscoveryDevice(DeviceModel deviceModel) {

        Database db = new Database();

        ArrayList<HashMap<String, String>> temp = null;

        HashMap<String, Object> rs = deviceModel.getResult();

        try {

            temp = db.databaseSelectOperation("select * from tbl_devices where type=? and ip=? and id != ?", new ArrayList<Object>(Arrays.asList(deviceModel.getType(), deviceModel.getIp(), deviceModel.getId())));

            if (temp.size() != 0) {

                rs.put("status", "Already exists");

                rs.put("code", Constants.ERROR);

                return;

            }

            if (deviceModel.getType().equals("ping")) {

                db.databaseDMLOperation("add", "insert into tbl_devices (deviceName,ip,type) values (?,?,?)", new ArrayList<Object>(Arrays.asList(deviceModel.getDeviceName(), deviceModel.getIp(), deviceModel.getType())));

            } else {

                db.databaseDMLOperation("add", "insert into tbl_devices (deviceName,ip,type,username,password) values (?,?,?,?,?)", new ArrayList<Object>(Arrays.asList(deviceModel.getDeviceName(), deviceModel.getIp(), deviceModel.getType(), deviceModel.getUsername(), deviceModel.getPassword())));

            }

            rs.put("status", deviceModel.getIp() + ": device added successfully");

            rs.put("code", Constants.SUCCESS);

        } catch (SQLException e) {

            rs.put("status", "Error occurred while adding");

            rs.put("code", Constants.ERROR);

            e.printStackTrace();

        }
    }

    public static void updateDiscoveryDevice(DeviceModel deviceModel) {

        Database db = new Database();

        HashMap<String, Object> rs = deviceModel.getResult();

        try {

            if (deviceModel.getType().equals("ping")) {

                db.databaseDMLOperation("update", "update tbl_devices set provision = false,deviceName = ?,ip = ? where id = ? ", new ArrayList<Object>(Arrays.asList(deviceModel.getDeviceName(), deviceModel.getIp(), String.valueOf(deviceModel.getId()))));

            } else {

                db.databaseDMLOperation("update", "update tbl_devices set provision = false,deviceName = ?,ip = ?,username = ?,password=? where id = ? ", new ArrayList<Object>(Arrays.asList(deviceModel.getDeviceName(), deviceModel.getIp(), deviceModel.getUsername(), deviceModel.getPassword(), String.valueOf(deviceModel.getId()))));

            }


            rs.put("status", deviceModel.getIp() + ": device updated successfully");

            rs.put("code", Constants.SUCCESS);

        } catch (SQLException e) {

            rs.put("status", "Error occurred while updating");

            rs.put("code", Constants.ERROR);

            e.printStackTrace();

        }
    }
}
