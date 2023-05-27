package hom.cluster.service.a.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import hom.cluster.common.base.code.BaseErrorCode;
import hom.cluster.common.base.res.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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
    private static final String JSON_TOKEN_HEADER = "X-Json-Token";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(HttpMethod.OPTIONS.matches(request.getMethod().toUpperCase())){
            //过滤OPTIONS请求
            filterChain.doFilter(request, response);
            return;
        }

        String JSONTokenBase64 = request.getHeader(JSON_TOKEN_HEADER);
        if (StringUtils.isBlank(JSONTokenBase64)){
            Result result = Result.failure(BaseErrorCode.UNAUTHORIZED);
            String body = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
            response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
            return;
        }else{
            //交由LoginUserArgumentResolver解析并注入接口
        }

        filterChain.doFilter(request, response);
    }
}
