package hom.cluster.service.b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//要使用外部的FeignClient,需要配置包的扫描路径
@EnableFeignClients(basePackages = "hom.cluster.feign.*")
@SpringBootApplication(scanBasePackages = "hom.cluster")
public class App2 {

    public static void main(String[] args) {
        SpringApplication.run(App2.class, args);
    }

}
