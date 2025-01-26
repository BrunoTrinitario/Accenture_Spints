package com.mindhub.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder  routeLocatorBuilder){
       return routeLocatorBuilder.routes()
               .route("user-service", r -> r.path("/API/users/**").uri("lb://user-service") )
               .route("product-service", r->r.path("/API/products/**").uri("lb://product-service"))
               .route("order-service", r->r.path("/API/orders/**").uri("lb://order-service"))
               .route("orderItem-service", r->r.path("/API/order-items/**").uri("lb://order-service"))
               .build();
    }
}
