package hom.cluster.auth.config.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author visy.wang
 * @description: 令牌存储配置(JWT)
 * @date 2023/5/23 22:54
 */
@Configuration
public class JwtTokenConfig {
    private static final String JWT_SIGNING_KEY = "hom.cluster.jwt.sign.key";

    @Bean
    public TokenStore jwtTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    //JwtToken转换器
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
}
