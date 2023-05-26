package hom.cluster.common.base.res;

import hom.cluster.common.base.code.BaseErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author visy.wang
 * @description: 通用返回体
 * @date 2023/5/24 15:17
 */
@Data
@AllArgsConstructor
public class Result {
    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    public static Result success(){
        return success(null);
    }
    public static Result success(Object data){
        return success(BaseErrorCodeEnum.SUCCESS, data);
    }
    public static Result success(BaseErrorCodeEnum codeEnum, Object data){
        return new Result(true, codeEnum.getCode(), codeEnum.getMsg(), data);
    }


    public static Result failure(){
        return failure(BaseErrorCodeEnum.FAILURE);
    }
    public static Result failure(String message){
        return failure(message, null);
    }
    public static Result failure(String message, Object data){
        return failure(BaseErrorCodeEnum.FAILURE.getCode(), message, data);
    }
    public static Result failure(BaseErrorCodeEnum codeEnum){
        return failure(codeEnum, null);
    }
    public static Result failure(BaseErrorCodeEnum codeEnum, Object data){
        return failure(codeEnum.getCode(), codeEnum.getMsg(), data);
    }
    public static Result failure(Integer code, String message){
        return failure(code, message, null);
    }
    public static Result failure(Integer code, String message, Object data){
        return new Result(false, code, message, data);
    }
}
