package hom.cluster.auth.config.tokenStore;

import hom.cluster.auth.component.JwtTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author visy.wang
 * @description: 令牌存储配置(JWT)
 * @date 2023/5/23 22:54
 */
//@Configuration
public class JwtTokenStoreConfig {
    private static final String JWT_SIGNING_KEY = "hom.cluster.jwt.sign.key";

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    //Jwt Token转换器
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

    //Token加强器，用于存储自己想要的信息到jwt中
    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }

    //token加强器链，把加强器和转换器加入链中
    @Bean
    public TokenEnhancerChain jwtTokenEnhancerChain(JwtTokenEnhancer jwtTokenEnhancer,
                                                 JwtAccessTokenConverter jwtAccessTokenConverter){
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(jwtTokenEnhancer);
        enhancers.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancers);
        return enhancerChain;
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
