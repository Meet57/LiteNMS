package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private Connection con;

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/litenms", "root", "password");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean DMLStatement(String operation, String query, ArrayList<String> values) throws SQLException {

        connect();
        PreparedStatement pst = con.prepareStatement(query);
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                pst.setString(i + 1, values.get(i));
            }
        }
        return pst.executeUpdate() > 0;
    }

    public ArrayList<HashMap<String, String>> select(String query, ArrayList<String> condition) throws SQLException {
        connect();

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

        return result;
    }
}
