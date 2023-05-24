package hom.cluster.gateway.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author visy.wang
 * @description:
 * @date 2023/5/24 15:20
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    FAILURE(0, "操作失败"),
    SUCCESS(1, "操作成功");

    private final Integer code;
    private final String msg;
}
