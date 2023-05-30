package com.liquorstore.cloud.gateway.config;

import com.liquorstore.cloud.gateway.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {
    @Autowired
    JwtFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("USER-SERVICE", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://USER-SERVICE"))
                .route("DEPARTMENT-SERVICE", r -> r.path("/departments/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://DEPARTMENT-SERVICE"))
                .route("SECURITY-SERVICE", r -> r.path("/security/**")
                        .uri("lb://SECURITY-SERVICE"))
                .build();
    }
}