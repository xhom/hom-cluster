package hom.cluster.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import hom.cluster.common.base.code.BaseErrorCode;
import hom.cluster.common.base.res.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
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
import java.util.Objects;

/**
 * @author visy.wang
 * @description: 全局过滤器
 * @date 2023/5/24 15:10
 */
@Slf4j
@Component
public class GatewayFilter implements GlobalFilter, Ordered {
    @Autowired
    private TokenStore tokenStore;

    @Override
    public int getOrder() {
        /*
         * 指定过滤器的优先级，数字越小优先级越高，默认按照声明顺序从1开始递增
         * 注意：
         * 一般情况下,这两种写法效果相同,但是在gateway中两者差别甚大
         * 如果使用@Order(-199)写法,会先执行gatewayFilter(虽然默认的order是10000),然后才执行GlobalFilter
         * 如果使用Ordered接口写法,会按照order值从小到大执行
         */
        return 0;
    }

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
            return unauthorized(exchange, "Token缺失");
        }

        //3 判断是否是有效的token
        OAuth2AccessToken accessToken;
        try {
            accessToken = tokenStore.readAccessToken(token);
            if(Objects.isNull(accessToken)){
                log.info("Token不存在：{}", token);
                return unauthorized(exchange, "无效Token", token);
            }

            if(accessToken.isExpired()){
                log.info("Token已过期：{}", token);
                return unauthorized(exchange, "Token已过期", token);
            }

            Map<String, Object> additionalInfo = accessToken.getAdditionalInformation();

            System.out.println("oAuth2AccessToken: "+ JSON.toJSONString(accessToken));
            System.out.println("additionalInfo: "+ JSON.toJSONString(additionalInfo));

            //取出用户身份信息
            Object principal = additionalInfo.get("user_name");
            //获取用户权限
            Object authorities = additionalInfo.get("authorities");

            JSONObject jsonToken = new JSONObject();
            jsonToken.put("principal", principal);
            jsonToken.put("authorities", authorities);

            //给header里面添加值
            String jsonTokenBase64 = Base64Utils.encodeToString(jsonToken.toJSONString().getBytes(StandardCharsets.UTF_8));
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate().header("JsonToken", jsonTokenBase64).build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
            return chain.filter(build);
        } catch (InvalidTokenException e) {
            log.info("Token无效: {}, error: {}", token, e.getMessage());
            return unauthorized(exchange, "无效Token", token);
        }catch (Exception e){
            log.info("Token解析异常: {}, error: {}", token, e.getMessage(), e);
            return unauthorized(exchange, "Token无法识别", token);
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

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message){
        return unauthorized(exchange, message, null);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message, Object data){
        return write2response(exchange, Result.failure(BaseErrorCode.UNAUTHORIZED.code(), message, data));
    }

    private Mono<Void> write2response(ServerWebExchange exchange, Result result){
        ServerHttpResponse response = exchange.getResponse();
        byte[] body = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(body);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
