package com.huawei.vi.gateway.filter;

import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.JwtTokenManager;
import com.jovision.jaws.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @ClassName : AuthorizationFilter
 * @Description : 全局token校验
 * @Author : pangxh
 * @Date: 2020-09-07 18:07
 */

@Slf4j
@Component
public class AuthorizationFilter implements GatewayFilter, Ordered {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${spring.mredis.securityKey}")
    private String securityKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst("Authorization");
        log.info("AuthorizationFilter url:{}  Authorization:{}",request.getURI(),authorization);
        if (StringUtils.isEmpty(authorization)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try {
            String token = authorization.split(" ")[1];
            Map<String, String> tokenFromRedis = JwtTokenManager.getTokenFromRedis(redisUtil, securityKey, token);
            if (tokenFromRedis == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            log.info("AuthorizationFilter   token:{}",token);
            ServerHttpRequest build = request.mutate().header("token", token).header("userId", tokenFromRedis.get("userId")).header("userName", tokenFromRedis.get("userName")).build();
            return chain.filter(exchange.mutate().request(build).build());
        } catch (Exception e) {
            log.error("AuthorizationFilter ",e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
