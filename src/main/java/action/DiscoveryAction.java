package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.opensymphony.xwork2.Action.SUCCESS;

public class DiscoveryAction extends ActionSupport {
    private int id;
    private String deviceName, ip, type, username, password;
    private HashMap<String, Object> result = new HashMap<>();

    public HashMap<String, Object> getResult() {
        return result;
    }

    public void setResult(HashMap<String, Object> result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        ArrayList<HashMap<String, String>> raw = db.select("select * from tbl_devices", null);
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        raw.forEach(ele -> {
            String html;
            if (ele.get("type").equals("ping")) {
                html = "<button class='btn btn-outline-success btn-sm' onclick=\"DISCOVERY.updateDeviceForm('" + ele.get("type") + "','" + ele.get("id") + "','" + ele.get("deviceName") + "','" + ele.get("ip") + "')\">EDIT</button><button class='btn btn-outline-danger btn-sm ms-2' onclick='DISCOVERY.deleteDeviceAction(" + ele.get("id") + ")'>DELETE</button><button class='btn btn-outline-primary btn-sm ms-2' onclick='PROVISION.getDiscovery(" + ele.get("id") + ")'>RUN</button>";
            } else {
                html = "<button class='btn btn-outline-success btn-sm' onclick=\"DISCOVERY.updateDeviceForm('" + ele.get("type") + "','" + ele.get("id") + "','" + ele.get("deviceName") + "','" + ele.get("ip") + "','" + ele.get("username") + "')\">EDIT</button><button class='btn btn-outline-danger btn-sm ms-2' onclick='DISCOVERY.deleteDeviceAction(" + ele.get("id") + ")'>DELETE</button><button class='btn btn-outline-primary btn-sm ms-2' onclick='PROVISION.getDiscovery(" + ele.get("id") + ")'>RUN</button>";
            }
            output.add(new ArrayList<String>(Arrays.asList(
                    ele.get("id"),
                    ele.get("deviceName"),
                    ele.get("ip"),
                    ele.get("type"),
                    html
            )));
        });

        result.put("result", output);

        return SUCCESS;
    }

    public String delete() throws Exception {
        Database db = new Database();

        db.DMLStatement("delete", "delete from tbl_devices where id = ?", new ArrayList<String>(Arrays.asList(String.valueOf(id))));

        return SUCCESS;
    }

    public String add() throws Exception {
        Database db = new Database();

        if (type.equals("ping")) {
            db.DMLStatement("add", "insert into tbl_devices (deviceName,ip,type) values (?,?,?)", new ArrayList<String>(Arrays.asList(deviceName, ip, type)));
        } else {
            db.DMLStatement("add", "insert into tbl_devices (deviceName,ip,type,username,password) values (?,?,?,?,?)", new ArrayList<String>(Arrays.asList(deviceName, ip, type, username, password)));
        }

        return SUCCESS;
    }

    public String update() throws Exception {
        Database db = new Database();

        if (type.equals("ping")) {
            db.DMLStatement("update", "update tbl_devices set deviceName = ?,ip = ? where id = ? ", new ArrayList<String>(Arrays.asList(deviceName, ip, String.valueOf(id))));
        } else {
            db.DMLStatement("update", "update tbl_devices set deviceName = ?,ip = ?,username = ?,password=? where id = ? ", new ArrayList<String>(Arrays.asList(deviceName, ip, username, password, String.valueOf(id))));
        }

        return SUCCESS;
    }
}
