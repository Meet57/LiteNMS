package services;

import DAO.Database;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class DashboardData {

    public static HashMap<String, Object> getDashboardData() {

        Database db = new Database();

        String date = String.valueOf(LocalDate.now()) + "%";

        HashMap<String, Object> result = new HashMap<>();

        try {

            result.put(
                    "cpu",
                    hashMapToHTML(db.select("select ip, max(cpu) as cpu from metrics where ( ts like ? and cpu is NOT NULL ) group by ip order by cpu desc limit 5;",
                            new ArrayList<>(Collections.singletonList(date))), new ArrayList<>(Arrays.asList("ip", "cpu")))
            );

            result.put(
                    "rtt",
                    hashMapToHTML(db.select("select ip, max(rtt) as rtt from metrics where ( ts like ? and rtt != -1 ) group by ip order by rtt desc limit 5;",
                            new ArrayList<>(Collections.singletonList(date))), new ArrayList<>(Arrays.asList("ip", "rtt")))
            );

            result.put(
                    "disk",
                    hashMapToHTML(db.select("select ip, max(disk) as disk from metrics where ( ts like ? and disk is NOT NULL ) group by ip order by disk desc limit 5;",
                            new ArrayList<>(Collections.singletonList(date))), new ArrayList<>(Arrays.asList("ip", "disk")))
            );

            HashMap<String, String> devices = new HashMap<>();

            ArrayList<HashMap<String, String>> ips = db.select("select ip from tbl_monitor_devices", null);

            for (HashMap<String, String> ip : ips) {
                devices.put(ip.get("ip"), "UNKNOWN");
            }

            ArrayList<HashMap<String, String>> metrics = db.select("SELECT ip,packetloss FROM metrics WHERE id IN (SELECT MAX(id) FROM metrics where ts like ? group by ip);", new ArrayList<>(Collections.singletonList(date)));

            if (metrics.size() > 0) {
                for (HashMap<String, String> metric : metrics) {
                    String status = metric.get("packetloss").equals("0") ? "UP" : "DOWN";
                    devices.put(metric.get("ip"), status);
                }
            }

            result.put("devices", hashMapTo2dArray(devices));


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return result;

    }

    public static ArrayList<ArrayList<String>> hashMapTo2dArray(HashMap<String, String> data) {
        ArrayList<ArrayList<String>> fina = new ArrayList();


        data.keySet().forEach(key -> {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(key);
            temp.add(data.get(key));
            fina.add(temp);
        });

        return fina;
    }

    public static String hashMapToHTML(ArrayList<HashMap<String, String>> data, ArrayList<String> columns) {

        StringBuilder sb = new StringBuilder();

        sb.append("<table class='w-100 table table-striped table-hover mt-3'><thead class='table-dark'><tr>");

        for (String col : columns) {
            sb.append("<td>").append(col).append("</td>");
        }

        sb.append("</tr></thead><tbody>");

        if (data.size() > 0) {
            for (HashMap<String, String> d : data) {

                sb.append("<tr>");

                for (String col : columns) {
                    sb.append("<td>").append(d.get(col)).append("</td>");
                }

                sb.append("</tr>");

            }

        } else {
            sb.append("<tr><td>First Polling is in process</td><td>Please wait for a while</td></tr>");
        }
        sb.append("</tbody></table>");

        return sb.toString();
    }

}
