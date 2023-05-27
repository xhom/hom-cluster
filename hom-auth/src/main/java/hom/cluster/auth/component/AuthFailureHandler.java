package hom.cluster.auth.component;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author visy.wang
 * @description: 认证失败处理器
 * @date 2023/5/24 13:44
 */
@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "登录失败: "+ e.getMessage());
        result.put("status", 500);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSON.toJSON(result));
    }
}
