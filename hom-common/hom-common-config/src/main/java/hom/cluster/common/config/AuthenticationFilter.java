package hom.cluster.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import hom.cluster.common.base.anno.NonAuth;
import hom.cluster.common.base.constants.HttpHeaderConst;
import hom.cluster.common.base.constants.SecretKeyConst;
import hom.cluster.common.base.enums.NonAuthPolicy;
import hom.cluster.common.base.res.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
                log.info("[权限过滤器]handlerChain为null");
                write(response, HttpStatus.INTERNAL_SERVER_ERROR, "服务异常");
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

            String GSK = request.getHeader(HttpHeaderConst.GATEWAY_SECRET);

            if(Objects.nonNull(nonAuth)){//无需登录
                String FSK = request.getHeader(HttpHeaderConst.FEIGN_SECRET);
                if(NonAuthPolicy.INNER.equals(nonAuth.value())){
                    //内部调用，比如Feign
                    //需要校验FeignSecretKey
                    if(SecretKeyConst.FEIGN_SECRET_KEY.equals(FSK)){
                        filterChain.doFilter(request, response);
                    }else{
                        log.info("[权限过滤器]FEIGN_SECRET_KEY错误");
                        write(response, HttpStatus.FORBIDDEN, "拒绝访问");
                    }
                }else if(NonAuthPolicy.OUTER.equals(nonAuth.value())){
                    //外部调用（通过gateway）
                    //需要校验GatewaySecretKey
                    if(SecretKeyConst.GATEWAY_SECRET_KEY.equals(GSK)){
                        filterChain.doFilter(request, response);
                    }else{
                        log.info("[权限过滤器]GATEWAY_SECRET_KEY错误（无需登录）");
                        write(response, HttpStatus.FORBIDDEN, "拒绝访问");
                    }
                }else if(NonAuthPolicy.ALL.equals(nonAuth.value())){
                    if(SecretKeyConst.FEIGN_SECRET_KEY.equals(FSK) || SecretKeyConst.GATEWAY_SECRET_KEY.equals(GSK)){
                        //满足其中一个Key即可
                        filterChain.doFilter(request, response);
                    }else{
                        log.info("[权限过滤器]FEIGN_SECRET_KEY或GATEWAY_SECRET_KEY（无需登录）错误");
                        write(response, HttpStatus.FORBIDDEN, "拒绝访问");
                    }
                }else{
                    log.info("[权限过滤器]无法识别NonAuthPolicy");
                    write(response, HttpStatus.FORBIDDEN, "拒绝访问");
                }
            }else{//需登录
                //这个Token由网关鉴权通过后写入Header
                String JSONToken = request.getHeader(HttpHeaderConst.JSON_TOKEN);
                if(!SecretKeyConst.GATEWAY_SECRET_KEY.equals(GSK)){
                    log.info("[权限过滤器]GATEWAY_SECRET_KEY错误(需登录)");
                    write(response, HttpStatus.FORBIDDEN, "拒绝访问");
                }else if (!StringUtils.hasText(JSONToken)){
                    log.info("[权限过滤器]用户未登录");
                    write(response, HttpStatus.UNAUTHORIZED, "用户未登录");
                }else{
                    //交由LoginUserArgumentResolver解析并注入接口
                    filterChain.doFilter(request, response);
                }
            }
        }catch (Exception e){
            log.info("[权限过滤器]系统异常：{}", e.getMessage(), e);
            write(response, HttpStatus.INTERNAL_SERVER_ERROR, "服务内部错误");
        }
    }

    private void write(HttpServletResponse response, HttpStatus status, String message) throws IOException{
        Result result = Result.failure(status.value(), message);
        String body = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
        response.setStatus(status.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
    }
}
