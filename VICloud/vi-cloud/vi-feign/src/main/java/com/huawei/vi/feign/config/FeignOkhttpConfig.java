package com.huawei.vi.feign.config;

import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @program: jaws
 * @description: feign的客户端替换成OkHttp
 * @author: pxh
 * @create: 2020-07-13 16:42
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkhttpConfig {

    @Bean
    public okhttp3.OkHttpClient client(){
        return  new OkHttpClient().newBuilder()
                 // 链接超时时间
                .connectTimeout(60, TimeUnit.SECONDS)
                 // 读超时
                .readTimeout(60,TimeUnit.SECONDS)
                 // 写超时
                .writeTimeout(60,TimeUnit.SECONDS)
                 // 重试
                .retryOnConnectionFailure(false)
                 // 连接池
                .connectionPool(new ConnectionPool())
                .build();
    }

}
