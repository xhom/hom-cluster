package hom.cluster.service.a.controller;

import com.alibaba.fastjson.JSON;
import hom.cluster.common.base.res.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/hello")
    public Result hello(HttpServletRequest request){
        System.out.println("user: "+ JSON.toJSONString(request.getAttribute("user")));
        return Result.success("Hello, Im Service A, And test's value is: "+ testValue);
    }
}
