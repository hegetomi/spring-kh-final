package com.hegetomi.gateway;

import com.hegetomi.tokenlib.JwtAuthFilter;
import com.hegetomi.tokenlib.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Component
@EnableWebFluxSecurity
public class JwtCheckingFilter implements GlobalFilter {


    @Autowired
    private JwtService jwtService;

    private PathPattern loginPathPattern = PathPatternParser.defaultInstance.parse("/users/login");
    private PathPattern orderPathPattern = PathPatternParser.defaultInstance.parse("/order/**");
    private PathPattern catalogPathPattern = PathPatternParser.defaultInstance.parse("/catalog/**");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Logger logger = LoggerFactory.getLogger(JwtCheckingFilter.class);
        Set<URI> origUrls = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI originalUri = origUrls.iterator().next();
        ServerHttpRequest request = exchange.getRequest();
        boolean orderUrlMatch = orderPathPattern.matches(PathContainer.parsePath(originalUri.toString()).subPath(4));
        boolean catalogUrlMatch = catalogPathPattern.matches(PathContainer.parsePath(originalUri.toString()).subPath(4));

        if (orderUrlMatch) {
            List<String> authHeaders = exchange.getRequest().getHeaders().get("Authorization");
            if (ObjectUtils.isEmpty(authHeaders))
                return send401Response(exchange);
            else {
                String authHeader = authHeaders.get(0);
                UsernamePasswordAuthenticationToken userDetails = null;
                try {
                    userDetails = JwtAuthFilter.userDetails(authHeader, jwtService);
                    if (userDetails != null) {
                        return chain.filter(exchange);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userDetails == null) {
                    return send401Response(exchange);
                }
            }
        }

        if (catalogUrlMatch) {
            List<String> authHeaders = exchange.getRequest().getHeaders().get("Authorization");
            if (ObjectUtils.isEmpty(authHeaders)) {
                if (request.getMethodValue().equals("GET")) {
                    return chain.filter(exchange);
                } else {
                    return send401Response(exchange);
                }
            }
            String authHeader = authHeaders.get(0);
            UsernamePasswordAuthenticationToken userDetails = null;
            try {
                userDetails = JwtAuthFilter.userDetails(authHeader, jwtService);
                if (userDetails != null) {
                    return chain.filter(exchange);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (userDetails == null && !request.getMethodValue().equals("GET")) {
                return send401Response(exchange);
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> send401Response(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .build();
    }
}


