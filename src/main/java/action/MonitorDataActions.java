package action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.MetricModel;
import services.MonitorDataService;

public class MonitorDataActions extends ActionSupport implements ModelDriven<MetricModel> {

    MetricModel result = new MetricModel();

    public String getMonitorData(){

        MonitorDataService.getMonitorData(result);

        return SUCCESS;

    }

    public String pollDevice(){

        MonitorDataService.pollDevice(result);

        return SUCCESS;

    }

    public String checkDeviceStatus() {

        MonitorDataService.checkDeviceStatus(result);

        return SUCCESS;
    }

    @Override
    public MetricModel getModel() {
        return result;
    }
}
