package hom.cluster.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hom.cluster.common.base.constants.HttpHeaderConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author visy.wang
 * @description: 登录用户参数解析器
 * @date 2023/5/27 13:58
 */
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private static HomConfig homConfig;
    @Autowired
    private SpringBeanContext springBeanContext;

    @Override
    public boolean supportsParameter(@NonNull MethodParameter methodParameter) {
        Class<?> loginUserBeanClass = getHomConfig().getLoginUserBeanClass();
        return Objects.nonNull(loginUserBeanClass) && loginUserBeanClass.equals(methodParameter.getParameterType());
    }


    @Override
    public Object resolveArgument(@Nullable MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if(Objects.isNull(request)){
            return null;
        }

        String JSONTokenBase64 = request.getHeader(HttpHeaderConst.JSON_TOKEN);
        if (!StringUtils.hasText(JSONTokenBase64)){
            return null;
        }

        String JSONToken = new String(Base64Utils.decodeFromString(JSONTokenBase64), StandardCharsets.UTF_8);
        JSONObject json = JSON.parseObject(JSONToken);
        return getHomConfig().getLoginUserBean(json);
    }

    private HomConfig getHomConfig(){
        if(Objects.nonNull(homConfig)){
            return homConfig;
        }
        homConfig = springBeanContext.getBean(HomConfig.class);
        if(Objects.isNull(homConfig)){
            homConfig = DefaultHomConfig.INSTANCE;
        }
        return homConfig;
    }
}
