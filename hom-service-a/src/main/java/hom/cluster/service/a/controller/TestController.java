package hom.cluster.service.a.controller;

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
    private String test;

    @RequestMapping("/hello")
    public String hello(){
        return test;
    }
}
