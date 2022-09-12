package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.MetricModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MetricAction extends ActionSupport implements ModelDriven<MetricModel> {

    MetricModel result = new MetricModel();

    public String get() throws Exception {
        Database db = new Database();
        HashMap<String, Object> rs = result.getResult();
        String date = String.valueOf(LocalDate.now())+"%";

        ArrayList<HashMap<String, String>> raw = db.select(
                "select * from metrics where ip = ? and type = ? and ts like ?",
                new ArrayList<>(Arrays.asList(result.getIp(), result.getType(), date))
        );

        if(raw.size() == 0){
            rs.put("code",0);
            rs.put("status","Polling Data is not available yet");

            return SUCCESS;
        }

        int availability = 0, mem = 0, tmem = 0;
        String disk = "";
        ArrayList<Integer> packets = new ArrayList<>();
        ArrayList<Integer> cpu = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();

        for (int i = 0; i < raw.size(); i++) {
            availability += Integer.parseInt(raw.get(i).get("packetloss"));
            packets.add(4 - Integer.parseInt(raw.get(i).get("packetloss")) / 25);
            time.add(raw.get(i).get("ts"));

            if (result.getType().equals("ssh")) {
                try {
                    cpu.add(100 - Integer.parseInt(raw.get(i).get("cpu")));
                    if (raw.get(i).get("disk") == null) {
                        disk = raw.get(i).get("disk");
                    }
                    mem = Integer.parseInt(raw.get(i).get("mem"));
                    tmem = Integer.parseInt(raw.get(i).get("tmem"));
                    disk = raw.get(i).get("disk");
                } catch (Exception e) {
                    cpu.add(0);
                }
            }
        }
        availability = 100 - availability / raw.size();

        rs.put("code",1);
        rs.put("availability", availability);
        rs.put("rtt", raw.get(raw.size() - 1).get("rtt"));
        rs.put("packets", packets);
        rs.put("mem", mem);
        rs.put("tmem", tmem);
        rs.put("disk", disk);
        rs.put("cpu", cpu);
        rs.put("time", time);

        return SUCCESS;
    }

    @Override
    public MetricModel getModel() {
        return result;
    }
}
