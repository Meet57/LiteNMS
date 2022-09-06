package action;

import DAO.Database;
import helper.Ping;
import helper.Polling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.opensymphony.xwork2.Action.SUCCESS;

public class PollingActions {
    private int id;
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

//    ABle to moniter or not for provision
    public String discovery() throws Exception {

        Database db = new Database();

        HashMap<String, String> data = db.select("select * from tbl_devices where id = ?",new ArrayList<>(Collections.singletonList(String.valueOf(id)))).get(0);

        Ping p;
        if(data.get("type").equals("ping")){
            p = new Ping();
            if(p.isUp(data.get("ip"))){
                result.put("status","Device up for polling");
                result.put("code",1);
            }else{
                result.put("status","Device not available for polling");
                result.put("code",0);
            }
        }else{
            p = new Ping();
            if(p.isUp(data.get("ip"))){
                Polling polling = new Polling();
                HashMap<String, String> metric = polling.polling(data.get("username"),data.get("password"),data.get("ip"),22);
                String resu = "Memory: " + metric.get("Mem") + "\nIdle CPU: " + metric.get("ICPU") + "\nStorage: " + metric.get("Storage");
                result.put("status",resu);
                result.put("code",1);
            }else{
                result.put("status","Device not available for polling");
                result.put("code",0);
            }
        }

        return SUCCESS;
    }
}
