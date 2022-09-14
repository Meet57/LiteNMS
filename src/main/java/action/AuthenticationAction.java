package action;

import DAO.Database;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.UserModel;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import java.util.*;

public class AuthenticationAction extends ActionSupport implements ModelDriven<UserModel>, SessionAware {

    UserModel user = new UserModel();
    private SessionMap<String,Object> sessionMap;

    public String login() throws Exception {

        ArrayList<HashMap<String, String>> existingData = new Database().select("select * from users where username = ?",
                new ArrayList<>(Collections.singletonList(user.getUsername()))
        );

        HashMap<String,Object> rs = user.getResult();

        if(existingData.size()==0 || !user.getPassword().equals(existingData.get(0).get("password"))){
            rs.put("status","Invalid Credentials");
            rs.put("code",0);

            return SUCCESS;
        }

        rs.put("username",user.getUsername());
        rs.put("status","login Successful");
        rs.put("code",1);

        sessionMap.put("login",true);
        sessionMap.put("username",user.getUsername());

        return SUCCESS;
    }

    public String logout(){

        sessionMap.remove("login");
        sessionMap.remove("username");

        return SUCCESS;
    }

    @Override
    public UserModel getModel() {
        return user;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        sessionMap=(SessionMap)map;
    }

}
