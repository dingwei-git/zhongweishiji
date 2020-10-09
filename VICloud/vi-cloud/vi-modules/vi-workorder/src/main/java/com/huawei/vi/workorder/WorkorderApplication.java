package com.huawei.vi.workorder;

import com.jovision.jaws.common.config.jwt.JwtTokenUtil;
import com.jovision.jaws.common.config.jwt.TikenUtil;
import com.jovision.jaws.common.config.redis.RedisConfig;
import com.jovision.jaws.common.config.redis.RedisOperatingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.huawei.vi.feign"})
@Import({RedisOperatingService.class, JwtTokenUtil.class, TikenUtil.class, RedisConfig.class})
public class WorkorderApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkorderApplication.class, args);
	}

	@PostConstruct
	void setDefaultTimezone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}
}
