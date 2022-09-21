package services;

import DAO.Database;
import model.UserModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LoginService {


    public static void login(UserModel userModel) {

        HashMap<String, Object> rs = userModel.getResult();

        ArrayList<HashMap<String, String>> existingData = null;

        try {

            existingData = new Database().databaseSelectOperation("select * from users where username = ?",
                    new ArrayList<>(Collections.singletonList(userModel.getUsername()))
            );

            if (existingData.size() == 0 || !userModel.getPassword().equals(existingData.get(0).get("password"))) {

                rs.put("status", "Invalid Credentials");

                rs.put("code", Constants.ERROR);

                return;
            }

            rs.put("username", userModel.getUsername());

            rs.put("status", "login Successful");

            rs.put("code", Constants.SUCCESS);

        } catch (SQLException e) {

            rs.put("status", "Server Error");

            rs.put("code", Constants.ERROR);

            e.printStackTrace();

        }
    }
}
