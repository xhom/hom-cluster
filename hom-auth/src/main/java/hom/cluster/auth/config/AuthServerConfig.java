package hom.cluster.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author visy.wang
 * @description: 认证服务配置
 * @date 2023/5/24 0:21
 */
@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    //客户端ID
    private static final String CLIENT_ID = "client_discovery";
    //客户端密匙
    private static final String CLIENT_SECRET = "secret_discovery";
    //资源ID
    private static final String RESOURCE_ID = "resource_discovery";
    //允许的授权范围
    private static final String[] SCOPES = { "app" };
    //AccessToken有效期（秒）
    private static final Integer ACCESS_TOKEN_VALIDITY_SECONDS = 3600;
    //RefreshToken有效期（秒）
    private static final Integer REFRESH_TOKEN_VALIDITY_SECONDS = 4800;
    //简化授权模式
    private static final String GRANT_TYPE_IMPLICIT = "implicit";
    //密码授权模式
    private static final String GRANT_TYPE_PASSWORD = "password";
    //Token刷新授权模式
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    //授权码模式
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";


    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    /*@Autowired
    @Qualifier("myClientDetailsService")
    private ClientDetailsService clientService;
    @Autowired
    private AuthorizationServerTokenServices tokenService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Bean("myClientDetailsService")
    public ClientDetailsService clientDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }*/

    /**
     * 配置客户端详细信息服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.withClientDetails(clientService);
        clients.inMemory() //使用内存存储
                .withClient(CLIENT_ID)
                .resourceIds(RESOURCE_ID)
                .secret(passwordEncoder.encode(CLIENT_SECRET))
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS)
                .scopes(SCOPES)
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, GRANT_TYPE_REFRESH_TOKEN);
    }

    /**
     * 认证服务器的安全配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //开启"/oauth/token_key"访问权限，提供公有密匙的端点，如果使用JWT令牌的话
                .tokenKeyAccess("permitAll()") //也可配置为：isAuthenticated()
                //开启"/oauth/check_token"访问权限，用于资源服务访问的令牌解析端点
                .checkTokenAccess("permitAll()") //也可配置为：isAuthenticated()
                //允许表单认证
                .allowFormAuthenticationForClients();
    }

    /**
     * 令牌访问端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);//Token获取的请求方式
               /* .authorizationCodeServices(authorizationCodeServices)
                .tokenServices(tokenService)
                .exceptionTranslator(new DefaultWebResponseExceptionTranslator());*/
    }
}
