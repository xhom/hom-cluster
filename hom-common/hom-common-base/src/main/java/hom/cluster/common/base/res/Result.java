package hom.cluster.common.base.res;

import hom.cluster.common.base.code.BaseErrorCode;
import hom.cluster.common.base.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author visy.wang
 * @description: 通用返回体
 * @date 2023/5/24 15:17
 */
@Data
@NoArgsConstructor //一定要写，序列化的时候需要
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
        return success(BaseErrorCode.SUCCESS, data);
    }
    public static Result success(String message, Object data){
        return new Result(true, BaseErrorCode.SUCCESS.getCode(), message, data);
    }
    public static Result success(ErrorCode errorCode, Object data){
        return new Result(true, errorCode.getCode(), errorCode.getMsg(), data);
    }

    public static Result failure(){
        return failure(BaseErrorCode.FAILURE);
    }
    public static Result failure(String message){
        return failure(message, null);
    }
    public static Result failure(String message, Object data){
        return failure(BaseErrorCode.FAILURE.getCode(), message, data);
    }
    public static Result failure(ErrorCode errorCode){
        return failure(errorCode, null);
    }
    public static Result failure(ErrorCode errorCode, Object data){
        return failure(errorCode.getCode(), errorCode.getMsg(), data);
    }
    public static Result failure(Integer code, String message){
        return failure(code, message, null);
    }
    public static Result failure(Integer code, String message, Object data){
        return new Result(false, code, message, data);
    }
}
