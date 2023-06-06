package hom.cluster.auth.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import hom.cluster.auth.common.OAuth2Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
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
import java.util.Objects;


/**
 * @author visy.wang
 * @description: 自定义退出成功处理器
 * @date 2023/5/31 19:14
 */
@Slf4j
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource(name="redisTokenStore")
    private TokenStore tokenStore;
    private static final String ACCESS_TOKEN_HEADER = "X-Access-Token";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        String token = request.getHeader(ACCESS_TOKEN_HEADER);
        log.info("退出登录: {}", token);

        OAuth2Result result;
        if(StringUtils.hasText(token)){
            //删除Token
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
            boolean isTokenExists = Objects.nonNull(accessToken);
            if(isTokenExists){
                tokenStore.removeAccessToken(accessToken);
            }
            result = OAuth2Result.success(1, "退出成功", isTokenExists?"T":"F");
        }else{
            result = OAuth2Result.failure(0, "退出失败", "Token缺失");
        }
        String body = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
    }
}
