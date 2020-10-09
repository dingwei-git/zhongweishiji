package com.huawei.vi.gateway.config;

import com.huawei.vi.gateway.filter.AuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: jaws
 * @description: Gateway的路由配置
 * @author: pxh
 * @create: 2020-07-14 10:51
 */
@Configuration
@Slf4j
public class ApiLocator {


    @Autowired
    private AuthorizationFilter authorizationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("thirddata_route",
                        r -> r
                                .path("/api/thirddata/**")
                                .filters(f -> f.stripPrefix(2).filter(authorizationFilter))
                                .uri("lb://vi-thirddata")
                )
                .route("map_route",
                        r -> r
                                .path("/api/map/**")
                                .filters(f -> f.stripPrefix(2).filter(authorizationFilter))
                                .uri("lb://vi-map")
                )
                .route("login_route",
                        r -> r
                                .path("/api/login/**")
                                .filters(f -> f.stripPrefix(2))
                                .uri("lb://vi-login")
                )
                .route("workorder_route",
                        r -> r
                                .path("/api/workorder/**")
                                .filters(f -> f.stripPrefix(2))
                                .uri("lb://vi-workorder")
                )
                .build();
    }

}
