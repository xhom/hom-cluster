package hom.cluster.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author visy.wang
 * @description: 从Spring容器获取Bean的上下文
 * @date 2023/5/28 21:26
 */
@Component
public class SpringBeanContext implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
    }

    /**
     * 通过指定Bean的类型获取实例
     * 适用于一个接口仅有一个实现类的情况
     * @param clazz Bean的类型
     * @return 指定类型的实例
     * @param <T> 具体类型
     */
    public <T> T getBean(Class<T> clazz){
        return Objects.isNull(context) ? null : context.getBean(clazz);
    }

    /**
     * 通过指定Bean的名称和类型获取实例
     * 适用于一个接口有多个实现类的情况
     * @param name Bean的名称（在容器中唯一）
     * @param clazz Bean的类型
     * @return 指定名称和类型的实例
     * @param <T> 具体类型
     */
    public <T> T getBean(String name, Class<T> clazz){
        return Objects.isNull(context) ? null : context.getBean(name, clazz);
    }
}
