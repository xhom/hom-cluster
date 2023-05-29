package hom.cluster.service.a.controller;

import com.alibaba.fastjson.JSON;
import hom.cluster.common.base.anno.NonAuth;
import hom.cluster.common.base.enums.NonAuthPolicy;
import hom.cluster.common.base.res.Result;
import hom.cluster.service.a.model.LoginUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author visy.wang
 * @description: 测试接口
 * @date 2023/5/23 11:52
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${hello}")
    private String helloValue;

    //@NonAuth注解说明：
    //* 这个注解标记的接口将不会验证用户登录，接收到的登录用户是null
    //1.接口提供给内部服务使用时：@NonAuth 或 @NonAuth(NonAuthPolicy.INNER)
    //2.接口暴露给外部（经网关转发）使用时：@NonAuth(NonAuthPolicy.OUTER)，但要在网关配置接口白名单
    //3.将接口同时暴露给内部服务和外部服务：@NonAuth(NonAuthPolicy.ALL)
    //4.这个注解也可以添加到Controller上，将对其中的所有接口生效，且优先级高于添加到方法上

    //这是一个供内部Feign调用的接口
    //这个接口只能给内部服务调用，外部无法调用（即使已登陆或加入白名单）
    @NonAuth
    //@NonAuth(NonAuthPolicy.INNER)
    @RequestMapping("/inner")
    public Result inner(LoginUser loginUser){
        System.out.println("inner, loginUser: "+ JSON.toJSONString(loginUser));
        return Result.success("Inner, Im Service A");
    }

    //开放给外部的接口，需在网关中添加到白名单，调用此接口无需登录，所以应谨慎使用
    //注意：内部服务无法调用此接口
    @NonAuth(NonAuthPolicy.OUTER)
    @RequestMapping("/outer")
    public Result outer(LoginUser loginUser){
        System.out.println("outer, loginUser: "+ JSON.toJSONString(loginUser));
        return Result.success("Outer, Im Service A");
    }

    //同时开放给外部和内部服务
    @NonAuth(NonAuthPolicy.ALL)
    @RequestMapping("/all")
    public Result all(LoginUser loginUser){
        System.out.println("all, loginUser: "+ JSON.toJSONString(loginUser));
        return Result.success("All, Im Service A");
    }

    //正常接口（需要登录的）
    @RequestMapping("/hello")
    public Result hello(LoginUser loginUser){
        System.out.println("hello, loginUser: "+ JSON.toJSONString(loginUser));
        return Result.success("Hello, Im Service A, And hello config value is: " + helloValue);
    }
}
