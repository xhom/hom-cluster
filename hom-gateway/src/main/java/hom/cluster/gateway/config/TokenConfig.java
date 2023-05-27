package hom.cluster.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author visy.wang
 * @description: 令牌存储配置
 * @date 2023/5/24 15:07
 */
@Configuration
public class TokenConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        //将Token保存到数据库
        return new JdbcTokenStore(dataSource);
    }
}
