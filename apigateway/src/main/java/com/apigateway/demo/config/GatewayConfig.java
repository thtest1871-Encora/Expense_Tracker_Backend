package com.apigateway.demo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://auth-service"))

                .route("transaction-service", r -> r.path("/transactions/**")
                        .uri("lb://transaction-service"))

                .route("notification-service", r -> r.path("/notifications/**")
                        .uri("lb://notification-service"))

                .route("category-service", r -> r.path("/categories/**")
                        .uri("lb://category-service"))
                
                .route("user-service", r -> r.path("/users/**")
                        .uri("lb://user-service"))
                
                .route("user-service", r -> r.path("/api/v1/vault/**")
                        .uri("lb://vaultservice"))
                
                .route("user-service", r -> r.path("/subscriptions/**")
                        .uri("lb://subscription-service"))

                .build();
    }
}
