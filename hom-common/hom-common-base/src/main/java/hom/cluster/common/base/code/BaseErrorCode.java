package hom.cluster.common.base.code;

import lombok.AllArgsConstructor;

/**
 * @author visy.wang
 * @description: 全局通用错误码（和某个服务的具体业务无关）
 * @date 2023/5/24 15:20
 */
@AllArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    FAILURE(0, "操作失败"),
    SUCCESS(1, "操作成功"),
    UNAUTHORIZED(401, "无访问权限");

    private final Integer code;
    private final String msg;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
