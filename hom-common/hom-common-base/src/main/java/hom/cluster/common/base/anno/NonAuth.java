package hom.cluster.common.base.anno;

import hom.cluster.common.base.enums.NonAuthPolicy;

import java.lang.annotation.*;

/**
 * @author visy.wang
 * @description: 标记无需鉴权的接口
 * @date 2023/5/28 10:51
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NonAuth {
    /**
     * 开放权限类型，默认开放给内部服务
     */
    NonAuthPolicy value() default NonAuthPolicy.INNER;
}
