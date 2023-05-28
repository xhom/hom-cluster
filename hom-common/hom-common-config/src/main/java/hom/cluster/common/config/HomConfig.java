package hom.cluster.common.config;

import com.alibaba.fastjson.JSONObject;

/**
 * @author visy.wang
 * @description: 项目自定义配置接口
 * @date 2023/5/28 21:19
 */
public interface HomConfig {
    /**
     * 获取登录用户的Class
     */
    Class<?> getLoginUserBeanClass();

    /**
     * 根据JSONToken获取登录用户信息
     */
    Object getLoginUserBean(JSONObject JSONToken);
}
