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
                String html = "<button class='btn btn-outline-success btn-sm editButton' data-id='" + ele.get("id") + "'>EDIT</button><button class='btn btn-outline-danger btn-sm ms-2 deleteButton' data-id='" + ele.get("id") + "'>DELETE</button><button class='btn btn-outline-primary btn-sm ms-2 runButton' data-id='" + ele.get("id") + "' data-type='" + ele.get("type") + "' data-ip='" + ele.get("ip") + "' >RUN</button>";
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

}
