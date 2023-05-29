package hom.cluster.common.base.code;

/**
 * @author visy.wang
 * @description: 错误码规范，所有项目中定义错误码时必须实现这个接口
 * @date 2023/5/26 23:00
 */
public interface ErrorCode {
    /**
     * 错误码
     */
    Integer getCode();

    /**
     * 错误信息
     */
    String getMsg();
}
