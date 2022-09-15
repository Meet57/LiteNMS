package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import helper.PingUtil;
import helper.PollingUtil;
import model.DeviceModel;
import services.PollingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
