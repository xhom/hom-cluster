package hom.cluster.common.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author visy.wang
 * @description: 全局通用错误码（和某个服务的具体业务无关）
 * @date 2023/5/24 15:20
 */
@Getter
@AllArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    FAILURE(0, "操作失败"),
    SUCCESS(1, "操作成功");

    private final Integer code;
    private final String msg;

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
