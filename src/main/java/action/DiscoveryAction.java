package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.DeviceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class DiscoveryAction extends ActionSupport implements ModelDriven<DeviceModel> {

    DeviceModel result = new DeviceModel();

    public String monitor() throws  Exception{
        Database db = new Database();

        ArrayList<HashMap<String, String>> raw = db.select("select id,deviceName,username,ip,type from tbl_devices where id = ?", new ArrayList<Object>(Collections.singletonList(result.getId())));

        HashMap<String, Object> rs = result.getResult();
        rs.put("data",raw.get(0));

        return SUCCESS;

    }
    public String get() throws Exception {
        Database db = new Database();

        ArrayList<HashMap<String, String>> raw = db.select("select * from tbl_devices", null);
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        raw.forEach(ele -> {
            String html = "<button class='btn btn-outline-success btn-sm editButton' data-id='"+ele.get("id")+"'>EDIT</button><button class='btn btn-outline-danger btn-sm ms-2 deleteButton' data-id='"+ele.get("id")+"'>DELETE</button><button class='btn btn-outline-primary btn-sm ms-2 runButton' data-id='"+ele.get("id")+"' data-type='"+ele.get("type")+"' data-ip='"+ele.get("ip")+"' >RUN</button>";
            if(Integer.parseInt(ele.get("provision")) >= 1 ){
                html += "<button class='btn btn-outline-success btn-sm ms-2 provisionButton' data-id='"+ele.get("id")+"'>PROVISION</button>";
            }
            output.add(new ArrayList<String>(Arrays.asList(
                    ele.get("deviceName"),
                    ele.get("ip"),
                    ele.get("type"),
                    html
            )));
        });

        result.putInResult("list", output);

        return SUCCESS;
    }

    public String delete() throws Exception {
        Database db = new Database();

        db.DMLStatement("delete", "delete from tbl_devices where id = ?", new ArrayList<Object>(Collections.singletonList(String.valueOf(result.getId()))));

        HashMap<String, Object> rs = result.getResult();
        rs.put("status", "Device deleted");
        rs.put("code", 0);

        return SUCCESS;
    }

    public String add() throws Exception {
        Database db = new Database();

        ArrayList<HashMap<String, String>> temp = db.select("select * from tbl_devices where type=? and ip=? and id != ?",new ArrayList<Object>(Arrays.asList(result.getType(),result.getIp(),result.getId())));

        HashMap<String, Object> rs = result.getResult();
        if(temp.size() != 0){
            rs.put("status","Already exists");
            rs.put("code",0);
            return SUCCESS;
        }

        if (result.getType().equals("ping")) {
            db.DMLStatement("add", "insert into tbl_devices (deviceName,ip,type) values (?,?,?)", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), result.getType())));
        } else {
            db.DMLStatement("add", "insert into tbl_devices (deviceName,ip,type,username,password) values (?,?,?,?,?)", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), result.getType(), result.getUsername(), result.getPassword())));
        }
        rs.put("status",result.getDeviceName()+": device added successfully");
        rs.put("code",1);

        return SUCCESS;
    }

    public String update() throws Exception {
        Database db = new Database();

        if (result.getType().equals("ping")) {
            db.DMLStatement("update", "update tbl_devices set provision = false,deviceName = ?,ip = ? where id = ? ", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), String.valueOf(result.getId()))));
        } else {
            db.DMLStatement("update", "update tbl_devices set provision = false,deviceName = ?,ip = ?,username = ?,password=? where id = ? ", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), result.getUsername(), result.getPassword(), String.valueOf(result.getId()))));
        }

        HashMap<String, Object> rs = result.getResult();
        rs.put("status",result.getDeviceName()+": device updated successfully");
        rs.put("code",1);

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
