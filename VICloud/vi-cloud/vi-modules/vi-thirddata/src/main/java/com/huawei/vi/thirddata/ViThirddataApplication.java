package com.huawei.vi.thirddata;

import com.jovision.jaws.common.anantation.EnableI18n;
import com.jovision.jaws.common.anantation.EnableMultiDataSource;
import com.jovision.jaws.common.anantation.EnableRedis;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.properties.TokenAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.MutablePropertySources;

@EnableRedis
@EnableI18n
@RefreshScope
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableMultiDataSource
@EnableFeignClients(basePackages = {"com.huawei.vi.feign"})
@Import({TokenAuth.class, MessageSourceUtil.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ViThirddataApplication {

    public static void main(String[] args) {
       SpringApplication.run(ViThirddataApplication.class, args);
    }

}
