package hom.cluster.auth.common;

/**
 * @author visy.wang
 * @description: 授权类型
 * @date 2023/5/31 15:19
 */
public class GrantTypes {
    //简化授权模式
    public static final String IMPLICIT = "implicit";
    //密码授权模式
    public static final String PASSWORD = "password";
    //Token刷新授权模式
    public static final String REFRESH_TOKEN = "refresh_token";
    //授权码模式
    public static final String AUTHORIZATION_CODE = "authorization_code";
}
