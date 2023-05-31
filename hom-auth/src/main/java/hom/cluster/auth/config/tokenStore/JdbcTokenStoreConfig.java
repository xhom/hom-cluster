package hom.cluster.auth.config.tokenStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author visy.wang
 * @description: 令牌存储配置(JDBC)
 * @date 2023/5/23 22:54
 */
@Configuration
public class JdbcTokenStoreConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore jdbcTokenStore() {
        //Token+认证信息的存储方式：
        //一共有以下五种实现方式：

        //InMemoryTokenStore：默认存储，保存在内存
        //JdbcTokenStore：access_token存储在数据库
        //JwkTokenStore：将 access_token 保存到 JSON Web Key
        //JwtTokenStore：这种方式比较特殊，这是一种无状态方式的存储，不进行内存、数据库存储，只是JWT中携带全面的用户信息，保存在jwt中携带过去校验就可以
        //RedisTokenStore：将 access_token 存到 redis 中

        return new JdbcTokenStore(dataSource);
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 客户端详情服务（存放在数据库）
     * @param dataSource 数据源
     * @param passwordEncoder 加密器
     */
    @Bean
    public ClientDetailsService myClientDetailsService(DataSource dataSource,
                                                     PasswordEncoder passwordEncoder) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /*
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

    /*
     * 授权码存储方式
     */
    /*@Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }*/

}
