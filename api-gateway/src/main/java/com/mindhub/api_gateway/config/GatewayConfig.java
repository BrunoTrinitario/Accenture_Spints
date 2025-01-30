package com.mindhub.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @GatewayConfig is a configuration class for setting up custom routes in a Spring Cloud Gateway.
 * It uses the @Configuration annotation to indicate that it is a source of bean definitions.
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AdminFilter adminFilter;

    /**
     * Defines a custom RouteLocator bean to configure routing for various microservices.
     *
     * This method creates a RouteLocator with routes for the following services:
     * 1. **User Service**: All requests starting with "/API/users/**" are routed to the `user-service`. Mainly used for creating, delete or update a user.
     * 2. **Product Service**: All requests starting with "/API/products/**" are routed to the `product-service`. Used for the creation, updating or delete of various products
     * 3. **Order Service**: All requests starting with "/API/orders/**" are routed to the `order-service`. The Main services to let users create orders.
     * 4. **Order Item Service**: All requests starting with "/API/order-items/**" are routed to the `order-service`. This services works as a debugged for the items that belongs to orders. Allowing
     *  to get every item form an order, updating or delete each one.
     *
     * Each route uses the **Load Balancer (lb://)** protocol to dynamically resolve the service instances.
     *
     * @param routeLocatorBuilder A builder for creating RouteLocator instances.
     * @return A custom RouteLocator with the defined routes.
     */
    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder  routeLocatorBuilder){
       return routeLocatorBuilder.routes()
               .route("user-service", r -> r.path("/API/users/**")
                       .filters(f -> f.filter(jwtAuthenticationFilter))
                       .uri("lb://user-service") )
               .route("admin-service", r -> r.path("/API/admin/**")
                       .filters(f -> f.filters(jwtAuthenticationFilter,adminFilter))
                       .uri("lb://user-service") )
               .route("auth-service", r -> r.path("/API/auth/**")
                       .uri("lb://user-service") )
               .route("product-service", r->r.path("/API/products/public/**")
                       .filters(f -> f.filter(jwtAuthenticationFilter))
                       .uri("lb://product-service"))
               .route("product-service", r->r.path("/API/products/admin/**")
                       .filters(f -> f.filters(jwtAuthenticationFilter,adminFilter))
                       .uri("lb://product-service"))
               .route("order-service", r->r.path("/API/orders/admin/**")
                       .filters(f -> f.filters(jwtAuthenticationFilter,adminFilter))
                       .uri("lb://order-service"))
               .route("order-service", r->r.path("/API/orders/user/**")
                       .filters(f -> f.filter(jwtAuthenticationFilter))
                       .uri("lb://order-service"))
               .route("orderItem-service", r->r.path("/API/order-items/user/**")
                       .filters(f -> f.filter(jwtAuthenticationFilter))
                       .uri("lb://order-service"))
               .build();
    }
}
