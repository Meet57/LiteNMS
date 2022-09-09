package helper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.dispatcher.SessionMap;

import java.util.Map;

public class MyInterceptor implements Interceptor {

    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }

    @Override
    public String intercept(ActionInvocation inv) throws Exception {

        ActionContext context = inv.getInvocationContext();

        SessionMap<String,Object> map = (SessionMap<String,Object>) inv.getInvocationContext().getSession();

        if(map==null)
        {
            return "login";
        }

        Object user = map.get("username");

        if(user==null ||user.equals("") || map.isEmpty() ){

            return "login";

        }

        inv.invoke();

        return "success";

    }
}
