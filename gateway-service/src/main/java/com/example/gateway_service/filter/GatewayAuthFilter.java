package com.example.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class GatewayAuthFilter extends AbstractGatewayFilterFactory<GatewayAuthFilter.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Extract the token from the Authorization header
            String token = extractTokenFromRequest(exchange.getRequest());

            // If token exists, add it to the request headers to pass to downstream service
            if (token != null) {
                exchange.getRequest().mutate()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build();
            }

            // Continue the filter chain
            return chain.filter(exchange);
        };
    }

    private String extractTokenFromRequest(org.springframework.web.server.ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    public static class Config {
        // Add any custom configuration properties for the filter if needed
    }
}

