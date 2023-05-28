package hom.cluster.common.base.anno;

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
     * 是否内部调用，比如Feign
     * 默认false,即为外部调用（通过gateway）
     */
    boolean isInner() default false;
}
