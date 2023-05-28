package hom.cluster.service.a.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import hom.cluster.common.base.anno.NonAuth;
import hom.cluster.common.base.code.BaseErrorCode;
import hom.cluster.common.base.constants.HttpHeaderConst;
import hom.cluster.common.base.constants.SecretKeyConst;
import hom.cluster.common.base.enums.NonAuthType;
import hom.cluster.common.base.res.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author visy.wang
 * @description: 权限过滤器
 * @date 2023/5/24 17:02
 */
@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(HttpMethod.OPTIONS.matches(request.getMethod().toUpperCase())){
            //过滤OPTIONS请求
            filterChain.doFilter(request, response);
            return;
        }

        try{
            HandlerExecutionChain handlerChain = requestMappingHandlerMapping.getHandler(request);
            if(Objects.isNull(handlerChain)){
                unauthorized(response, "服务异常");
                return;
            }

            HandlerMethod methodHandler = (HandlerMethod) handlerChain.getHandler();

            //检查注解
            NonAuth nonAuth = null;
            Class<?> beanType = methodHandler.getBeanType();
            if(beanType.isAnnotationPresent(NonAuth.class)){
                //优先从类上获取
                nonAuth = beanType.getAnnotation(NonAuth.class);
            }else if(methodHandler.getMethod().isAnnotationPresent(NonAuth.class)){
                //类上获取不到时从方法获取
                nonAuth = methodHandler.getMethod().getAnnotation(NonAuth.class);
            }

            if(Objects.nonNull(nonAuth)){//无需登录
                String FSK = request.getHeader(HttpHeaderConst.FEIGN_SECRET_KET);
                String GSK = request.getHeader(HttpHeaderConst.GATEWAY_SECRET_KET);

                if(NonAuthType.INNER.equals(nonAuth.type())){
                    //内部调用，比如Feign
                    //需要校验FeignSecretKey(自定义的连接密码)
                    if(SecretKeyConst.FEIGN_SECRET_KEY.equals(FSK)){
                        filterChain.doFilter(request, response);
                    }else{
                        unauthorized(response, "拒绝连接");
                    }
                }else if(NonAuthType.OUTER.equals(nonAuth.type())){
                    //外部调用（通过gateway）
                    //需要校验GatewaySecretKey(自定义的连接密码)
                    if(SecretKeyConst.GATEWAY_SECRET_KEY.equals(GSK)){
                        filterChain.doFilter(request, response);
                    }else{
                        unauthorized(response, "拒绝连接");
                    }
                }else{ //NonAuth.NonAuthType.ALL
                    if(SecretKeyConst.FEIGN_SECRET_KEY.equals(FSK) || SecretKeyConst.GATEWAY_SECRET_KEY.equals(GSK)){
                        //满足其中一个Key即可
                        filterChain.doFilter(request, response);
                    }else{
                        unauthorized(response, "拒绝连接");
                    }
                }
            }else{//需登录
                //这个Token由网关鉴权通过后写入Header
                String JSONTokenBase64 = request.getHeader(HttpHeaderConst.JSON_TOKEN);
                if (StringUtils.isBlank(JSONTokenBase64)){
                    //理论上不会为空
                    //除了一些不需要登录认证的接口，但需在网关中配置
                    unauthorized(response, "用户未登录");
                }else{
                    //交由LoginUserArgumentResolver解析并注入接口
                    filterChain.doFilter(request, response);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            unauthorized(response, e.getMessage());
        }
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException{
        Result result = Result.failure(BaseErrorCode.UNAUTHORIZED.getCode(), message);
        String body = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
    }
}
