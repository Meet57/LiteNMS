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

//        StringBuilder column = new StringBuilder();
//
//        for(String c : columns) {
//            column.append(c).append(",");
//        }
//
//        column.deleteCharAt(column.length()-1);
//
//        StringBuilder query = new StringBuilder("select ? from ? where ?");
//        query.replace(query.indexOf("?"),query.indexOf("?")+1,column.toString());
//        query.replace(query.indexOf("?"),query.indexOf("?")+1,tableName);
//        query.replace(query.indexOf("?"),query.indexOf("?")+1,condition);
//
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery(query.toString());
//
//        HashMap<String,String> temp = null;
//
//        ResultSetMetaData rsMetaData = rs.getMetaData();
//        System.out.println("List of column names in the current table: ");
//        //Retrieving the list of column names
//        int count = rsMetaData.getColumnCount();
//        for(int i = 1; i<=count; i++) {
//            System.out.println(rsMetaData.getColumnName(i));
//        }
//
//        while(rs.next()){
//            temp = new HashMap<>();
//            for (int i = 0; i < columns.size(); i++) {
//                temp.put(columns.get(i),rs.getString(i+1));
//            }
//            result.add(temp);
//        }

        return result;
    }
}
