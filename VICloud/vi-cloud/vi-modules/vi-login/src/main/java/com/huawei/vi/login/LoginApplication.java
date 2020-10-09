package com.huawei.vi.login;

import com.jovision.jaws.common.anantation.EnableI18n;
import com.jovision.jaws.common.anantation.EnableMultiDataSource;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.config.jwt.JwtTokenUtil;
import com.jovision.jaws.common.config.jwt.TikenUtil;
import com.jovision.jaws.common.config.redis.RedisConfig;
import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.config.redisListener.APPKeyExpiredListener;
import com.jovision.jaws.common.config.redisListener.KeyExpiredListener;
import com.jovision.jaws.common.config.redisListener.RedisConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RefreshScope
@EnableDiscoveryClient
@EnableI18n
@EnableMultiDataSource
@EnableFeignClients(basePackages = {"com.huawei.vi.feign"})
@Import({RedisOperatingService.class, JwtTokenUtil.class, TikenUtil.class, RedisConfig.class, MessageSourceUtil.class,RedisConfiguration.class,KeyExpiredListener.class, APPKeyExpiredListener.class})
public class LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}

}
