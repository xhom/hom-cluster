package hom.cluster.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author visy.wang
 * @description: 自定义配置
 * @date 2023/5/28 13:20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "hom.gateway")
public class HomGatewayConfig  {
    private List<String> urlWhiteList;
}
