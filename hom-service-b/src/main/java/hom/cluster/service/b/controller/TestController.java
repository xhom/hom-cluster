package hom.cluster.service.b.controller;

import hom.cluster.common.base.res.Result;
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

    @RequestMapping("/hello")
    public Result hello(){
        return Result.success("Hello, Im Service B.");
    }
}
