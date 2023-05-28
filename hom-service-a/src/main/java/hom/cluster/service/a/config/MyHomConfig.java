package hom.cluster.service.a.config;

import com.alibaba.fastjson.JSONObject;
import hom.cluster.common.config.HomConfig;
import hom.cluster.service.a.model.LoginUser;
import org.springframework.stereotype.Component;

/**
 * @author visy.wang
 * @description: 自定义本项目相关配置
 * @date 2023/5/28 21:41
 */
@Component
public class MyHomConfig implements HomConfig {
    @Override
    public Class<?> getLoginUserBeanClass() {
        return LoginUser.class;
    }

    @Override
    public Object getLoginUserBean(JSONObject JSONToken) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(JSONToken.getLong("userid"));
        loginUser.setUsername(JSONToken.getString("username"));
        //可添加更多字段...
        return loginUser;
    }
}
