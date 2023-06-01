package hom.cluster.auth.component;

import hom.cluster.auth.common.OAuth2Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;
import java.util.Objects;

/**
 * @author visy.wang
 * @description: 认证异常翻译器
 * @date 2023/5/27 15:35
 */
@Component
public class AuthExceptionTranslator implements WebResponseExceptionTranslator {
    private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<?> translate(Exception e) throws Exception {
        System.out.println("AuthExceptionTranslator: "+ e.getMessage()+"["+e.getClass().getName()+"]");

        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

        //认证异常
        OAuth2Exception oa2e = getFromChain(causeChain, OAuth2Exception.class);
        if(Objects.nonNull(oa2e)){
            return toReturn(oa2e, oa2e.getHttpErrorCode(), "登录失败");
        }

        //身份验证相关异常
        AuthenticationException ae = getFromChain(causeChain, AuthenticationException.class);
        if(Objects.nonNull(ae)){
            return toReturn(ae, HttpStatus.UNAUTHORIZED.value(), "身份验证失败");
        }

        //拒绝访问异常
        AccessDeniedException ade = getFromChain(causeChain, AccessDeniedException.class);
        if(Objects.nonNull(ade)){
            return toReturn(ade, HttpStatus.FORBIDDEN.value(), "拒绝访问");
        }

        //请求方式异常
        HttpRequestMethodNotSupportedException hme = getFromChain(causeChain, HttpRequestMethodNotSupportedException.class);
        if(Objects.nonNull(hme)){
            return toReturn(hme, HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方式不支持");
        }

        //服务器异常
        return toReturn(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常");
    }

    private ResponseEntity<?> toReturn(Exception e, Integer status, String message) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Pragma", "no-cache");
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        OAuth2Result result = OAuth2Result.failure(status, message, e.getMessage());
        return new ResponseEntity<>(result, headers, HttpStatus.valueOf(status));
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> E getFromChain(Throwable[] causeChain, Class<E> eClass){
        return (E)this.throwableAnalyzer.getFirstThrowableOfType(eClass, causeChain);
    }
}
