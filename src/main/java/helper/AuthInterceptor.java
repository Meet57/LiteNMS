package helper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;

import java.util.Map;

public class AuthInterceptor implements Interceptor {

    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {

        Map<String, Object> sessionMain = ActionContext.getContext().getSession();

        boolean result = false;

        if (sessionMain.get("username") != null)
        {
            actionInvocation.invoke();

            result = true;
        }

        if (!result)
        {
            return "login";
        }

        return ActionSupport.SUCCESS;

    }
}
