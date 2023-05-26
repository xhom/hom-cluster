package hom.cluster.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hom.cluster.gateway.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author visy.wang
 * @description:
 * @date 2023/5/24 15:10
 */
@Slf4j
@Order(0)//数字越小优先级越高，默认按照声明顺序从1开始递增
@Component
public class GatewayFilter implements GlobalFilter {
    @Autowired
    private TokenStore tokenStore;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        //1 auth服务所有放行
        if (pathMatcher.match("/auth/**", requestUrl)) {
            return chain.filter(exchange);
        }

        //2 检查token是否存在
        String token = getToken(exchange);
        if (StringUtils.isBlank(token)) {
            Result result = Result.failure(HttpStatus.UNAUTHORIZED.value(), "Token缺失");
            return write2response(exchange, result);
        }

        //3 判断是否是有效的token
        OAuth2AccessToken oAuth2AccessToken;
        try {
            oAuth2AccessToken = tokenStore.readAccessToken(token);

            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            //取出用户身份信息
            Object principal = additionalInformation.get("user_name");
            //获取用户权限
            Object authorities = additionalInformation.get("authorities");

            JSONObject jsonToken = new JSONObject();
            jsonToken.put("principal", principal);
            jsonToken.put("authorities", authorities);

            //给header里面添加值
            String jsonTokenBase64 = Base64Utils.encodeToString(jsonToken.toJSONString().getBytes(StandardCharsets.UTF_8));
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate().header("JsonToken", jsonTokenBase64).build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
            return chain.filter(build);
        } catch (InvalidTokenException e) {
            log.info("无效的token: {}, error: {}", token, e.getMessage());
            Result result = Result.failure(HttpStatus.UNAUTHORIZED.value(), "无效Token", token);
            return write2response(exchange, result);
        }catch (Exception e){
            log.info("token解析失败: {}, error: {}", token, e.getMessage());
            Result result = Result.failure(HttpStatus.UNAUTHORIZED.value(), "无效Token", token);
            return write2response(exchange, result);
        }
    }

    /**
     * 获取Token
     */
    private String getToken(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst("Authorization");
        if (StringUtils.isNotBlank(token)) {
            String[] tokenArray = token.split(" ");
            if (tokenArray.length > 1) {
                token = tokenArray[1];
            }else{
                token = tokenArray[0];
            }
            if(StringUtils.isNotBlank(token)){
                return token;
            }
        }
        return null;
    }

    private Mono<Void> write2response(ServerWebExchange exchange, Result result){
        ServerHttpResponse response = exchange.getResponse();
        byte[] body = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(body);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
