package hom.cluster.auth.config;

import hom.cluster.auth.common.GrantTypes;
import hom.cluster.auth.component.AuthExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;


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

    @Resource(name="redisTokenStore")
    private TokenStore tokenStore;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthExceptionTranslator authExceptionTranslator;
    @Autowired
    private ClientDetailsService myClientDetailsService;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    /*
    @Autowired
    private AuthorizationServerTokenServices tokenService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    */

    /**
     * 配置客户端详细信息服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.withClientDetails(myClientDetailsService); //使用数据库存储
        clients.inMemory() //使用内存存储
                .withClient(CLIENT_ID)
                .resourceIds(RESOURCE_ID)
                .secret(passwordEncoder.encode(CLIENT_SECRET)) //存在数据库的时候同样需要先加密
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS)
                .scopes(SCOPES)
                .authorizedGrantTypes(GrantTypes.PASSWORD, GrantTypes.REFRESH_TOKEN);
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
        if(JwtTokenStore.class.equals(tokenStore.getClass())){
            configure4Jwt(endpoints);
        }else{
            configure4JdbcOrRedis(endpoints);
        }
    }

    @SuppressWarnings("unchecked")
    private void configure4JdbcOrRedis(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(authExceptionTranslator) //自定义异常处理
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); //Token获取的请求方式
    }

    @SuppressWarnings("unchecked")
    private void configure4Jwt(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter)//jwtToken转换器
                .exceptionTranslator(authExceptionTranslator) //自定义异常处理
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); //Token获取的请求方式
    }
}
