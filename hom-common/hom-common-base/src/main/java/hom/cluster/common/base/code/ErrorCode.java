package hom.cluster.common.base.code;

/**
 * @author visy.wang
 * @description: 错误码规范
 * @date 2023/5/26 23:00
 */
public interface ErrorCode {
    /**
     * 错误码
     */
    Integer code();

    /**
     * 错误信息
     */
    String msg();
}
