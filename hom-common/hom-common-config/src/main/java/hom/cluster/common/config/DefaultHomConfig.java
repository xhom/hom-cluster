package hom.cluster.common.config;

import com.alibaba.fastjson.JSONObject;

/**
 * @author visy.wang
 * @description: HomConfig默认实现
 * @date 2023/5/28 21:24
 */
public class DefaultHomConfig implements HomConfig {
    public static final HomConfig INSTANCE = new DefaultHomConfig();

    @Override
    public Class<?> getLoginUserBeanClass() {
        return null;
    }

    @Override
    public Object getLoginUserBean(JSONObject JSONToken) {
        return null;
    }
}
