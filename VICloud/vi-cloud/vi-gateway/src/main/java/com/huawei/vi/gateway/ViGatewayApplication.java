package com.huawei.vi.gateway;

import com.jovision.jaws.common.anantation.EnableRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient  // nacos服务发现
@RefreshScope    // nacos 配置
@EnableRedis
@SpringBootApplication
@EnableFeignClients(basePackages = "com.huawei.vi.feign")
public class ViGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViGatewayApplication.class, args);
    }

}
