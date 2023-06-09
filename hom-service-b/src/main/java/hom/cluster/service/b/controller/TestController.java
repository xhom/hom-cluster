package hom.cluster.service.b.controller;

import hom.cluster.common.base.res.Result;
import hom.cluster.feign.a.client.TestFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author visy.wang
 * @description: 测试接口
 * @date 2023/5/26 22:43
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestFeignClient testFeignClient;


    /*
     * 注意：项目内的接口不可以和Feign中的的路径冲突
     */
    @RequestMapping("/hello")
    public Result hello(){
        return Result.success("Hello, Im Service B");
    }

    @RequestMapping("/feign1")
    public Result feign1(){
        Result feignData = testFeignClient.inner();
        return Result.success("Feign1, Im Service B.", feignData);
    }

    @RequestMapping("/feign2")
    public Result feign2(){
        Result feignData = testFeignClient.all();
        return Result.success("Feign2, Im Service B.", feignData);
    }
}
