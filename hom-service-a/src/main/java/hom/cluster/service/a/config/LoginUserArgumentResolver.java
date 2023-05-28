package hom.cluster.service.a.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hom.cluster.common.base.constants.HttpHeaderConst;
import hom.cluster.service.a.model.LoginUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
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
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return LoginUser.class.equals(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if(Objects.isNull(request)){
            return null;
        }

        String JSONTokenBase64 = request.getHeader(HttpHeaderConst.JSON_TOKEN_HEADER);
        if (StringUtils.isBlank(JSONTokenBase64)){
            return null;
        }

        String JSONToken = new String(Base64Utils.decodeFromString(JSONTokenBase64), StandardCharsets.UTF_8);
        JSONObject data = JSON.parseObject(JSONToken);
        LoginUser loginUser = new LoginUser();
        loginUser.setId(data.getLong("userid"));
        loginUser.setUsername(data.getString("username"));
        //可添加更多字段...
        return loginUser;
    }
}
