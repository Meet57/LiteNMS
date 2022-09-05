package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.opensymphony.xwork2.Action.SUCCESS;

public class DiscoveryAction extends ActionSupport {
    private int id;
    private String deviceName,ip;
    private HashMap<String, Object> result = new HashMap<>();

    public HashMap<String, Object> getResult() {
        return result;
    }

    public void setResult(HashMap<String, Object> result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String get() throws Exception {
        Database db = new Database();

//        ArrayList<HashMap<String,String>> raw = db.select(new ArrayList<String>(Arrays.asList("id","deviceName","ip")),"tbl_ping_devices","1=1");
        ArrayList<HashMap<String,String>> raw = db.select("select * from tbl_devices",null);
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        raw.forEach(ele->{
            output.add(new ArrayList<String>(Arrays.asList(
                    ele.get("id"),
                    ele.get("deviceName"),
                    ele.get("ip"),
                    "<button class='btn btn-outline-success btn-sm' onclick=\"DISCOVERY.updateDeviceForm(\'ping\','"+ele.get("id")+"','"+ele.get("deviceName")+"','"+ele.get("ip")+"')\">EDIT</button><button class='btn btn-outline-danger btn-sm ms-2' onclick='DISCOVERY.deleteDeviceAction("+ele.get("id")+")'>DELETE</button>"
            )));
        });

        result.put("result",output);

        return SUCCESS;
    }

    public String delete() throws Exception{
        Database db = new Database();

        db.DMLStatement("delete","tbl_ping_devices",id,null);

        return SUCCESS;
    }

    public String add() throws Exception{
        Database db = new Database();

        HashMap<String,String> data = new HashMap<>();
        data.put("query","insert into tbl_ping_devices (deviceName, ip) values ('"+deviceName+"','"+ip+"')");
        db.DMLStatement("add","tbl_ping_devices",0,data);

        return SUCCESS;
    }
}
