package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.DeviceModel;
import services.MonitorDiscoveryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MonitorDiscoveryAction extends ActionSupport implements ModelDriven<DeviceModel> {

    DeviceModel result = new DeviceModel();

    public String getSingleDiscoveryDevice(){

        MonitorDiscoveryService.getSingleDiscoveryDevice(result);

        return SUCCESS;

    }
    public String getAllDiscoveryDevices(){

        MonitorDiscoveryService.getAllDiscoveryDevices(result);

        return SUCCESS;
    }

    public String deleteDiscoveryDevice(){

        MonitorDiscoveryService.deleteDiscoveryDevice(result);

        return SUCCESS;
    }

    public String addDiscoveryDevice() throws Exception {

        MonitorDiscoveryService.addDiscoveryDevice(result);

        return SUCCESS;
    }

    public String updateDiscoveryDevice() throws Exception {

        MonitorDiscoveryService.updateDiscoveryDevice(result);

        return SUCCESS;
    }

    @Override
    public DeviceModel getModel() {
        return result;
    }
}
