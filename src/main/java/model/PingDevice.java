package model;

public class PingDevice {
    private int id;
    private String name,ip;

    public PingDevice() {
    }

    public PingDevice(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public PingDevice(int id, String name, String ip) {
        this.id = id;
        this.name = name;
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
