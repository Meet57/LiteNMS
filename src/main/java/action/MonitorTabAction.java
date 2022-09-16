package action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import helper.CacheData;
import model.DeviceModel;
import services.MonitorTabService;

public class MonitorTabAction extends ActionSupport implements ModelDriven<DeviceModel> {

    DeviceModel result = new DeviceModel();

    public String getMonitorDevices(){

        MonitorTabService.getMonitorDevices(result);

        return SUCCESS;
    }

    public String deleteMonitorDevice(){

        MonitorTabService.deleteMonitorDevice(result);

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
