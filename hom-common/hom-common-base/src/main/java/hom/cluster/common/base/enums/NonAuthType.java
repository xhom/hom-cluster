package hom.cluster.common.base.enums;

/**
 * @author visy.wang
 * @description: 权限开发类型枚举
 * @date 2023/5/28 14:00
 */
public enum NonAuthType {
    // OUTER + INNER
    ALL,
    // 开放给外部服务（Gateway）
    OUTER,
    // 开放给内部服务（Feign）
    INNER
}
