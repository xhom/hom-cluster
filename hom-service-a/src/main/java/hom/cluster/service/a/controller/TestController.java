package hom.cluster.service.a.controller;

import com.alibaba.fastjson.JSON;
import hom.cluster.common.base.anno.NonAuth;
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
    @Value("${test}")
    private String testValue;

    //* 这个注解标记的接口将不会验证用户登录，接收到的登录用户是null
    //1.接口提供给内部服务使用时：@NonAuth(isInner = true)
    //2.接口暴露给外部（经网关转发）使用时：@NonAuth 或 @NonAuth(isInner = false)，但要在网关配置接口白名单
    //3.这个注解也可以添加到Controller上，将对其中的所有接口生效，且优先级高于添加到方法上
    @NonAuth(isInner = true)
    @RequestMapping("/hello") //这是一个供内部Feign调用的服务
    public Result hello(LoginUser loginUser){
        System.out.println("hello, loginUser: "+ JSON.toJSONString(loginUser));
        return Result.success("Hello, Im Service A, And test's value is: "+ testValue);
    }

    //开放给外部的服务，需在网关中添加到白名单
    @NonAuth
    @RequestMapping("/open")
    public Result open(LoginUser loginUser){
        System.out.println("open, loginUser: "+ JSON.toJSONString(loginUser));
        return Result.success("Open, Im Service A");
    }

    //正常接口（需要登录的）
    @RequestMapping("/haha")
    public Result haha(LoginUser loginUser){
        System.out.println("haha, loginUser: "+ JSON.toJSONString(loginUser));
        return Result.success("Haha, Im Service A");
    }
}
