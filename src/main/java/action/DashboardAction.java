package action;

import com.opensymphony.xwork2.ActionSupport;
import services.DashboardData;

import java.util.HashMap;

public class DashboardAction extends ActionSupport {
    HashMap<String,Object> result = new HashMap<>();

    public HashMap<String, Object> getResult() {
        return result;
    }

    public void setResult(HashMap<String, Object> result) {
        this.result = result;
    }

    public String get() throws  Exception{

        result.put("result", DashboardData.getDashboardData());

        return SUCCESS;
    }
}
