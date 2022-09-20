package services;

import DAO.Database;
import helper.CacheData;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DashboardDataService {

    public static HashMap<String, Object> getDashboardData() {

        Database db = new Database();

        String startDate = String.valueOf(LocalDate.now()) + " 00:00:00";

        String endDate = String.valueOf(LocalDate.now()) + " 23:59:59";

        HashMap<String, Object> result = new HashMap<>();

        try {

            result.put(
                    "cpu",
                    hashMapToHTML(db.databaseSelectOperation("select ip, max(cpu) as cpu from metrics where ( (timestamp between ? and ? ) and cpu is NOT NULL ) group by ip order by cpu desc limit 5;",
                            new ArrayList<>(Arrays.asList(startDate,endDate))), new ArrayList<>(Arrays.asList("ip", "cpu")), new ArrayList<>(Arrays.asList("IP", "CPU in %")))
            );

            result.put(
                    "rtt",
                    hashMapToHTML(db.databaseSelectOperation("select ip, max(rtt) as rtt from metrics where ( (timestamp between ? and ? ) and rtt != -1 ) group by ip order by rtt desc limit 5;",
                            new ArrayList<>(Arrays.asList(startDate,endDate))), new ArrayList<>(Arrays.asList("ip", "rtt")),new ArrayList<>(Arrays.asList("IP", "RTT in ms")))
            );

            result.put(
                    "disk",
                    hashMapToHTML(db.databaseSelectOperation("select ip, max(disk) as disk from metrics where ( (timestamp between ? and ? ) and disk is NOT NULL ) group by ip order by disk desc limit 5;",
                            new ArrayList<>(Arrays.asList(startDate,endDate))), new ArrayList<>(Arrays.asList("ip", "disk")),new ArrayList<>(Arrays.asList("IP", "Disk usage in %")))
            );

            HashMap<String, Object> devices = new HashMap<>(CacheData.getData());

            ArrayList<ArrayList<Object>> deviceStatus = new ArrayList<>();

            for (String ip : devices.keySet()) {
                deviceStatus.add(new ArrayList<>(Arrays.asList(ip, devices.get(ip))));
            }

            result.put("devices", deviceStatus);


        } catch (SQLException e) {

            e.printStackTrace();

        }

        return result;

    }

    public static String hashMapToHTML(ArrayList<HashMap<String, String>> data, ArrayList<String> columns, ArrayList<String> heading) {

        StringBuilder sb = new StringBuilder();

        if (data.size() > 0) {

            sb.append("<table class='w-100 table table-striped table-hover mt-3'><thead class='table-dark'><tr>");

            for (String col : heading) {
                sb.append("<td>").append(col).append("</td>");
            }

            sb.append("</tr></thead><tbody>");

            for (HashMap<String, String> d : data) {

                sb.append("<tr>");

                for (String col : columns) {
                    sb.append("<td>").append(d.get(col)).append("</td>");
                }

                sb.append("</tr>");

            }

            sb.append("</tbody></table>");

        } else {

            sb.append("<h3 class='text-danger'>N/A</h3>");

        }

        return sb.toString();
    }

}
