package hom.cluster.feign.a.client;

import hom.cluster.common.base.res.Result;
import hom.cluster.feign.a.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author visy.wang
 * @description: 测试Feign客户端（对应一个Controller）
 * @date 2023/5/26 12:35
 */
@FeignClient(value = "hom-service-a", configuration = MultipartSupportConfig.class)
@RequestMapping("/test")
public interface TestFeignClient {
    @RequestMapping("/hello")
    Result hello();
}
