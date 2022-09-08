package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private Connection con;

    static {
        ConnectionPool.createConnectionPool(5);
    }

    public Boolean DMLStatement(String operation, String query, ArrayList<String> values) throws SQLException {

        con = ConnectionPool.getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                pst.setString(i + 1, values.get(i));
            }
        }

        boolean result = pst.executeUpdate() > 0;
        ConnectionPool.putConnection(con);
        return result;
    }

    public ArrayList<HashMap<String, String>> select(String query, ArrayList<String> condition) throws SQLException {

        con = ConnectionPool.getConnection();

        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        PreparedStatement pst = con.prepareStatement(query);

        if (condition != null) {
            for (int i = 0; i < condition.size(); i++) {
                pst.setString(i + 1, condition.get(i));
            }
        }

        ResultSet rs = pst.executeQuery();
        ArrayList<String> columnNames = new ArrayList<>();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int count = rsMetaData.getColumnCount();
        for (int i = 1; i <= count; i++) {
            columnNames.add(rsMetaData.getColumnName(i));
        }

        HashMap<String, String> temp;
        while (rs.next()) {
            temp = new HashMap<>();
            for (String columnName : columnNames) {
                temp.put(columnName, rs.getString(columnName));
            }
            result.add(temp);
        }

        ConnectionPool.putConnection(con);
        return result;
    }
}
