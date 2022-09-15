package action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.DeviceModel;
import services.PollingService;

public class PollingAction extends ActionSupport implements ModelDriven<DeviceModel> {
    DeviceModel result = new DeviceModel();

    public String discovery(){

        PollingService.discovery(result);

        return SUCCESS;
    }

    public String putDeviceForMonitoring(){

        PollingService.putDeviceForMonitoring(result);

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
