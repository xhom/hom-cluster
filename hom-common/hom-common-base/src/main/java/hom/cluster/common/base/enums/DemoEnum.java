package hom.cluster.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author visy.wang
 * @description: Demo枚举
 * @date 2023/5/26 0:17
 */
@Getter
@AllArgsConstructor
public enum DemoEnum {
    TEST1(1, "test1"),
    TEST2(2, "test2");


    private final Integer code;
    private final String desc;
}
