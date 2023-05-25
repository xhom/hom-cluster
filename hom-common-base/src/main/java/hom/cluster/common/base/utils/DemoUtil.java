package hom.cluster.common.base.utils;

import java.util.Objects;

/**
 * @author visy.wang
 * @description: Demo工具函数
 * @date 2023/5/26 0:19
 */
public class DemoUtil {

    public static String toLowerCase(String val){
        return Objects.isNull(val) ? null : val.toLowerCase();
    }
}
