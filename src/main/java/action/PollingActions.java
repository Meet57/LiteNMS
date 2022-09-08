package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import helper.Ping;
import helper.Polling;
import model.DeviceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class PollingActions extends ActionSupport implements ModelDriven<DeviceModel> {
    DeviceModel result = new DeviceModel();

    //    ABle to monitor or not for provision
    public String discovery() throws Exception {

        Database db = new Database();
        HashMap<String, Object> rs = result.getResult();

        Ping p;
        if (result.getType().equals("ping")) {
            p = new Ping();
            if (p.isUp(result.getIp())) {
                rs.put("status", "Device up for polling");
                rs.put("code", 1);
                db.DMLStatement("update", "update tbl_devices set provision = 1 where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(result.getId()))));
            } else {
                rs.put("status", "Device not available for polling");
                rs.put("code", 0);
            }
        } else {
            p = new Ping();
            if (p.isUp(result.getIp())) {
                Polling polling = new Polling();

                HashMap<String,String> data = db.select("select username,password from tbl_devices where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(result.getId())))).get(0);

                HashMap<String, String> metric = polling.polling(data.get("username"), data.get("password"), result.getIp(), 22);
                if (metric.get("code").equals("1")) {
                    rs.put("status", "Device up for polling");
                    rs.put("code", 1);
                    db.DMLStatement("update", "update tbl_devices set provision = 1 where id = ?", new ArrayList<>(Arrays.asList(String.valueOf(result.getId()))));
                } else {
                    rs.put("status", metric.get("status"));
                    rs.put("code", 0);
                }
            } else {
                rs.put("status", "Device not available for polling");
                rs.put("code", 0);
            }
        }

        return SUCCESS;
    }

    public String put() throws Exception {

        Database db = new Database();
        HashMap<String, Object> rs = result.getResult();

        HashMap<String, String> data = db.select("select * from tbl_devices where id = ?", new ArrayList<>(Collections.singletonList(String.valueOf(result.getId())))).get(0);

        ArrayList<HashMap<String, String>> exsitingDevices = db.select("select * from tbl_monitor_devices where type = ? and ip = ?", new ArrayList<String>(Arrays.asList(data.get("type"), data.get("ip"))));

        db.DMLStatement("update", "update tbl_devices set provision = 2 where id = ?", new ArrayList<>(Collections.singletonList(String.valueOf(result.getId()))));

        if (exsitingDevices.size() > 0) {

            db.DMLStatement("update", "update tbl_monitor_devices set deviceName = ?,username = ? , password = ? where id = ?",
                    new ArrayList<>(Arrays.asList(data.get("deviceName"),  data.get("username"), data.get("password"), exsitingDevices.get(0).get("id")))
            );

            rs.put("status", data.get("deviceName") + ": device updated in monitoring");
            rs.put("code", 1);

            return SUCCESS;
        }

        db.DMLStatement("add", "insert into tbl_monitor_devices (deviceName,ip,type,username,password) values (?,?,?,?,?)",
                new ArrayList<String>(Arrays.asList(data.get("deviceName"), data.get("ip"), data.get("type"), data.get("username"), data.get("password")))
        );

        rs.put("status", data.get("deviceName") + ": device added for monitoring");
        rs.put("code", 1);

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
