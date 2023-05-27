package hom.cluster.service.a.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author visy.wang
 * @description: 权限过滤器
 * @date 2023/5/24 17:02
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(HttpMethod.OPTIONS.matches(request.getMethod().toUpperCase())){
            //过滤OPTIONS请求
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("JsonToken");
        if (StringUtils.isNotBlank(token)){
            String jsonTokenStr = Base64Utils.encodeToString(token.getBytes(StandardCharsets.UTF_8));
            //JSONObject jsonToken = JSON.parseObject(jsonTokenStr);
            System.out.println("jsonToken: "+ jsonTokenStr);
        }

        filterChain.doFilter(request, response);
    }
}
