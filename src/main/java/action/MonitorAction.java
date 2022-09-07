package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.DeviceModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MonitorAction extends ActionSupport implements ModelDriven<DeviceModel> {

    DeviceModel result = new DeviceModel();

    public String get() throws Exception {
        Database db = new Database();
        StringBuilder html = new StringBuilder();
        HashMap<String, Object> rs = result.getResult();

        ArrayList<HashMap<String, String>> raw = db.select("select * from tbl_monitor_devices", null);

        for (HashMap<String,String> row : raw) {
            html.append("<div class=\"border rounded border-1 my-2 p-2 d-flex justify-content-between align-items-center\">\n" +
                    "<div><span class=\"badge mx-1 text-bg-dark\">"+row.get("type")+"</span>"+
                    row.get("ip") +
                    "                </div><div>\n" +
                    "                    <button class=\"btn btn-outline-primary btn-sm\">View</button>\n" +
                    "                    <button class=\"btn btn-outline-danger ms-1 btn-sm\">Delete</button>\n" +
                    "                </div>\n" +
                    "            </div>");
        }

        rs.put("result",html.toString());

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
