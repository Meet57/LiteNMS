package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.DeviceModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MonitorAction extends ActionSupport implements ModelDriven<DeviceModel> {

    DeviceModel result = new DeviceModel();

    public String get() throws Exception {

        Database db = new Database();
        HashMap<String, Object> rs = result.getResult();
        String date = String.valueOf(LocalDate.now()) + "%";

        ArrayList<HashMap<String, String>> raw = db.select("select * from tbl_monitor_devices", null);
        HashMap<String, String> devices = new HashMap<>();
        ArrayList<ArrayList<String>> output = new ArrayList<>();

        for (HashMap<String, String> ip : raw) {
            devices.put(ip.get("ip"), "<i class=\"bi bi-arrow-repeat\"></i>");
        }

        ArrayList<HashMap<String, String>> metrics = db.select("SELECT ip,packetloss FROM metrics WHERE id IN (SELECT MAX(id) FROM metrics where ts like ? group by ip);", new ArrayList<>(Collections.singletonList(date)));

        if (metrics.size() > 0) {
            for (HashMap<String, String> metric : metrics) {
                String status = metric.get("packetloss").equals("0") ? "<i class=\"bi text-success bi-arrow-up\"></i>" : "<i class=\"bi text-danger bi-arrow-down\"></i>";
                devices.put(metric.get("ip"), status);
            }
        }

        String html;
        for (HashMap<String,String> ele : raw) {

            html = "<button class='btn btn-outline-primary btn-sm viewMonitorButton' data-ip='"+ele.get("ip")+"' data-type='"+ele.get("type")+"'>VIEW</button><button class='btn btn-outline-danger btn-sm ms-2 deleteMonitorButton' data-id='"+ele.get("id")+"'>DELETE</button>";

            output.add(new ArrayList<String>(Arrays.asList(
                    ele.get("deviceName"),
                    devices.get(ele.get("ip"))+" "+ele.get("ip"),
                    ele.get("type"),
                    html
            )));
        }

        rs.put("list",output);

        return SUCCESS;
    }

    public String delete() throws Exception {
        Database db = new Database();

        db.DMLStatement("delete", "delete from tbl_monitor_devices where id = ?", new ArrayList<Object>(Collections.singletonList(String.valueOf(result.getId()))));

        HashMap<String, Object> rs = result.getResult();
        rs.put("status", "Device deleted");
        rs.put("code", 0);

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
