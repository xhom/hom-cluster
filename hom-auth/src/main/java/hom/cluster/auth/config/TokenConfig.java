package hom.cluster.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * @author visy.wang
 * @description: 令牌存储配置
 * @date 2023/5/23 22:54
 */
@Configuration
public class TokenConfig {
    @Autowired
    private DataSource dataSource;
    private static final String JWT_SIGNING_KEY = "hom.cluster.jwt.sign.key";

    @Bean
    public TokenStore tokenStore() {
        //Token+认证信息的存储方式：
        //一共有以下五种实现方式：

        //InMemoryTokenStore：默认存储，保存在内存
        //JdbcTokenStore：access_token存储在数据库
        //JwkTokenStore：将 access_token 保存到 JSON Web Key
        //JwtTokenStore：这种方式比较特殊，这是一种无状态方式的存储，不进行内存、数据库存储，只是JWT中携带全面的用户信息，保存在jwt中携带过去校验就可以
        //RedisTokenStore：将 access_token 存到 redis 中

        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        /*
         *   JWT校验规则:
         *   1、设置jwt和signingkey(自定义，加密解密保持一致即可)
         *   2、按"."对jwt分成三部分，即: header、payload、sign
         *   3、取第一部分进行base64解码，获取加密方式
         *   4、取第二部分进行base64解码，获取业务参数，即payload
         *   5、使用加密方式和signingkey创建校验器，对header+payload进行加密，并与sign (即第=部分) 进行对比
         *   6、取出payload的有效期进行校验，是否过期通过校验，返回claims信息
         */
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(JWT_SIGNING_KEY);
        return converter;
    }

    /**
     * 配置令牌管理
     */
    /*@Bean
    public AuthorizationServerTokenServices tokenService(ClientDetailsService clientDetailsService,
                                                         TokenStore tokenStore,
                                                         JwtAccessTokenConverter accessTokenConverter) {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        return service;
    }*/

    /**
     * 授权码存储方式
     */
    /*@Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }*/

}
