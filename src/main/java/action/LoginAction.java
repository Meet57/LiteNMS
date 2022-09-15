package action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import model.UserModel;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;
import services.LoginService;

import java.util.*;

public class LoginAction extends ActionSupport implements ModelDriven<UserModel>, SessionAware {

    UserModel user = new UserModel();
    private SessionMap<String,Object> sessionMap;

    public String login(){

        LoginService.login(user);

        if(user.getResult().get("code").equals(1)){

            sessionMap.put("login",true);

            sessionMap.put("username",user.getUsername());
        }

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
