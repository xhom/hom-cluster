package hom.cluster.auth.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author visy.wang
 * @description: 自定义退出成功处理器
 * @date 2023/5/31 19:14
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource(name="redisTokenStore")
    private TokenStore tokenStore;
    private static final String ACCESS_TOKEN_HEADER = "X-Access-Token";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        String token = request.getHeader(ACCESS_TOKEN_HEADER);
        System.out.println("token: "+ token);

        JSONObject result = new JSONObject();
        if(StringUtils.hasText(token)){
            //删除储存的认证数据
            tokenStore.readAccessToken(token);
            result.put("success", true);
            result.put("code", 1);
            result.put("message", "退出成功");
        }else{
            result.put("success", false);
            result.put("code", 0);
            result.put("message", "退出失败");
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String body = JSON.toJSONString(result);
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
    }
}
