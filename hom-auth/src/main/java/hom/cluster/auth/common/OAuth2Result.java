package hom.cluster.auth.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author visy.wang
 * @description: 自定义认证结果
 * @date 2023/5/27 16:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Result {
    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    public static OAuth2Result success(Integer code, String message){
        return success(code, message, null);
    }
    public static OAuth2Result success(Integer code, String message, Object data){
        return new OAuth2Result(true, code, message, data);
    }

    public static OAuth2Result failure(Integer code, String message){
        return failure(code, message, null);
    }
    public static OAuth2Result failure(Integer code, String message, Object data){
        return new OAuth2Result(false, code, message, data);
    }
}
