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

import static com.opensymphony.xwork2.Action.SUCCESS;

public class PollingActions extends ActionSupport implements ModelDriven<DeviceModel> {
    DeviceModel result = new DeviceModel();

//    ABle to monitor or not for provision
    public String discovery() throws Exception {

        Database db = new Database();

        HashMap<String, String> data = db.select("select * from tbl_devices where id = ?",new ArrayList<>(Collections.singletonList(String.valueOf(result.getId())))).get(0);

        HashMap<String, Object> rs = result.getResult();
        Ping p;
        if(data.get("type").equals("ping")){
            p = new Ping();
            if(p.isUp(data.get("ip"))){
                rs.put("status","Device up for polling");
                rs.put("code",1);
                new Database().DMLStatement("update","update tbl_devices set provision = 1 where id = ?",new ArrayList<>(Arrays.asList(String.valueOf(result.getId()))));
            }else{
                rs.put("status","Device not available for polling");
                rs.put("code",0);
            }
        }else{
            p = new Ping();
            if(p.isUp(data.get("ip"))){
                Polling polling = new Polling();
                HashMap<String, String> metric = polling.polling(data.get("username"),data.get("password"),data.get("ip"),22);
                if(metric.get("code").equals("1")){
                    String resu = "Memory: " + metric.get("Mem") + "\nIdle CPU: " + metric.get("ICPU") + "\nStorage: " + metric.get("Storage");
                    rs.put("status",resu);
                    rs.put("code",1);
                    new Database().DMLStatement("update","update tbl_devices set provision = 1 where id = ?",new ArrayList<>(Arrays.asList(String.valueOf(result.getId()))));
                }else{
                    rs.put("status",metric.get("status"));
                    rs.put("code",0);
                }
            }else{
                rs.put("status","Device not available for polling");
                rs.put("code",0);
            }
        }

        return SUCCESS;
    }

    public String put() throws Exception {

        Database db = new Database();

        HashMap<String, String> data = db.select("select * from tbl_devices where id = ?",new ArrayList<>(Collections.singletonList(String.valueOf(result.getId())))).get(0);

        db.DMLStatement("add", "insert into tbl_monitor_devices (deviceName,ip,type,username,password) values (?,?,?,?,?)", new ArrayList<String>(Arrays.asList(data.get("deviceName"), data.get("ip"), data.get("type"), data.get("username"), data.get("password"))));

        HashMap<String, Object> rs = result.getResult();
        rs.put("status",data.get("deviceName")+": device added for monitoring");
        rs.put("code",1);

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
