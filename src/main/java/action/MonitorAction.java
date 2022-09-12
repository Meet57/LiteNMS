package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.DeviceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MonitorAction extends ActionSupport implements ModelDriven<DeviceModel> {

    DeviceModel result = new DeviceModel();

    public String get() throws Exception {
        Database db = new Database();
        HashMap<String, Object> rs = result.getResult();

        ArrayList<HashMap<String, String>> raw = db.select("select * from tbl_monitor_devices", null);
        ArrayList<ArrayList<String>> output = new ArrayList<>();

        String html;
        for (HashMap<String,String> ele : raw) {

            html = "<button class='btn btn-outline-primary btn-sm viewMonitorButton' data-ip='"+ele.get("ip")+"' data-type='"+ele.get("type")+"'>VIEW</button><button class='btn btn-outline-danger btn-sm ms-2 deleteMonitorButton' data-id='"+ele.get("id")+"'>DELETE</button>";

            output.add(new ArrayList<String>(Arrays.asList(
                    ele.get("deviceName"),
                    ele.get("ip"),
                    ele.get("type"),
                    html
            )));
        }

        rs.put("list",output);

        return SUCCESS;
    }

    public String delete() throws Exception {
        Database db = new Database();

        db.DMLStatement("delete", "delete from tbl_monitor_devices where id = ?", new ArrayList<String>(Collections.singletonList(String.valueOf(result.getId()))));

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
