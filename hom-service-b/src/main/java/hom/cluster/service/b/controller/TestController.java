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
    @RequestMapping("/hello2")
    public Result hello(){
        Result feignData = testFeignClient.hello();
        return Result.success("Hello, Im Service B.", feignData);
    }
}
