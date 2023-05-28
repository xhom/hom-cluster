package hom.cluster.common.base.constants;

/**
 * @author visy.wang
 * @description: 平台通用密匙常量
 * @date 2023/5/28 11:22
 */
public class SecretKeyConst {
    /**
     * 内部服务调用需要的密码（自定义）
     */
    public static final String FEIGN_SECRET_KEY = "FEIGN_SECRET_KEY";
    /**
     * 网关无登录时调用服务的密码（自定义）
     */
    public static final String GATEWAY_SECRET_KEY = "GATEWAY_SECRET_KEY";
}