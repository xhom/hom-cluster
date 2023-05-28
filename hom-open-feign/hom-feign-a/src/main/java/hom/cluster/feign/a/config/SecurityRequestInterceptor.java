package hom.cluster.feign.a.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import hom.cluster.common.base.constants.HttpHeaderConst;
import hom.cluster.common.base.constants.SecretKeyConst;
import org.springframework.stereotype.Component;

/**
 * @author visy.wang
 * @description: 请求安全拦截器
 * @date 2023/5/28 12:23
 */
@Component
public class SecurityRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //添加安全密码到请求头
        requestTemplate.header(HttpHeaderConst.FEIGN_SECRET_KET, SecretKeyConst.FEIGN_SECRET_KEY);
    }
}
