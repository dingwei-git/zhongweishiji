package com.huawei.vi.map;

import com.jovision.jaws.common.anantation.EnableMultiDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RefreshScope
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableMultiDataSource
@EnableFeignClients(basePackages = {"com.huawei.vi.feign"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ViMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViMapApplication.class, args);
    }

}
