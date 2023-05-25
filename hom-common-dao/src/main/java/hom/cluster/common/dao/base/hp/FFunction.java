package hom.cluster.common.dao.base.hp;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface FFunction<T,R> extends Function<T,R>, Serializable {
    //通过Getter方法引用获取属性名所需的函数是接口
}
