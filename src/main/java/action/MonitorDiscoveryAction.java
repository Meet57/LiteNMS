package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.DeviceModel;
import services.MonitorDiscoveryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MonitorDiscoveryAction extends ActionSupport implements ModelDriven<DeviceModel> {

    DeviceModel result = new DeviceModel();

    public String getSingleDiscoveryDevice(){

        MonitorDiscoveryService.getSingleDiscoveryDevice(result);

        return SUCCESS;

    }
    public String getAllDiscoveryDevices(){

        System.out.println("MonitorDiscoveryAction");

        MonitorDiscoveryService.getAllDiscoveryDevices(result);

        return SUCCESS;
    }

    public String deleteDiscoveryDevice() throws Exception {
        Database db = new Database();

        db.databaseDMLOperation("delete", "delete from tbl_devices where id = ?", new ArrayList<Object>(Collections.singletonList(String.valueOf(result.getId()))));

        HashMap<String, Object> rs = result.getResult();
        rs.put("status", "Device deleted");
        rs.put("code", 0);

        return SUCCESS;
    }

    public String addDiscoveryDevice() throws Exception {
        Database db = new Database();

        ArrayList<HashMap<String, String>> temp = db.databaseSelectOperation("select * from tbl_devices where type=? and ip=? and id != ?",new ArrayList<Object>(Arrays.asList(result.getType(),result.getIp(),result.getId())));

        HashMap<String, Object> rs = result.getResult();
        if(temp.size() != 0){
            rs.put("status","Already exists");
            rs.put("code",0);
            return SUCCESS;
        }

        if (result.getType().equals("ping")) {
            db.databaseDMLOperation("add", "insert into tbl_devices (deviceName,ip,type) values (?,?,?)", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), result.getType())));
        } else {
            db.databaseDMLOperation("add", "insert into tbl_devices (deviceName,ip,type,username,password) values (?,?,?,?,?)", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), result.getType(), result.getUsername(), result.getPassword())));
        }
        rs.put("status",result.getDeviceName()+": device added successfully");
        rs.put("code",1);

        return SUCCESS;
    }

    public String updateDiscoveryDevice() throws Exception {
        Database db = new Database();

        if (result.getType().equals("ping")) {
            db.databaseDMLOperation("update", "update tbl_devices set provision = false,deviceName = ?,ip = ? where id = ? ", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), String.valueOf(result.getId()))));
        } else {
            db.databaseDMLOperation("update", "update tbl_devices set provision = false,deviceName = ?,ip = ?,username = ?,password=? where id = ? ", new ArrayList<Object>(Arrays.asList(result.getDeviceName(), result.getIp(), result.getUsername(), result.getPassword(), String.valueOf(result.getId()))));
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
