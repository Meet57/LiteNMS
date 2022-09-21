package helper;

import DAO.ConnectionPool;
import DAO.Database;
import services.Constants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CacheData {
    public static ConcurrentHashMap<String,Object> data = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Object> getData() {
        return data;
    }

    public static void setData(ConcurrentHashMap<String, Object> data) {
        CacheData.data = data;
    }

    public static void loadMonitorDevices(){

        try {

            ArrayList<HashMap<String, String>> devices = new Database().databaseSelectOperation("select ip from tbl_monitor_devices;",null);

            for (HashMap<String, String> device: devices) {

                data.put(device.get("ip"), Constants.UNKNOWN);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public static void excludeMonitorDevices(){

        try {

            ArrayList<HashMap<String, String>> devices = new Database().databaseSelectOperation("select ip from tbl_monitor_devices;",null);

            data.keySet().retainAll(Arrays.asList(devices.stream().map((device) -> device.get("ip")).toArray()));

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
