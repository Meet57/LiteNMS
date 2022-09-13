package model;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceModel {
    private int id;
    private String type,deviceName,ip,username,password,socketId;

    private HashMap<String, Object> result;

    public DeviceModel() {
        result = new HashMap<>();
    }

    public DeviceModel(int id, String deviceName, String ip) {
        this();
        this.id = id;
        this.deviceName = deviceName;
        this.ip = ip;
    }

    public DeviceModel(int id, String deviceName, String ip, String username, String password) {
        this();
        this.id = id;
        this.deviceName = deviceName;
        this.ip = ip;
        this.username = username;
        this.password = password;
    }

    public HashMap<String, Object> getResult() {
        return result;
    }

    public void setResult(HashMap<String, Object> result) {
        this.result = result;
    }
    public void putInResult(String key,Object value) {
        result.put(key,value);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }
}
