package com.phoenix.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author ZeroZhao
 * @version 1.0
 * @title: AuthFilter
 * @projectName security
 * @description: TODO
 * @date 2020/10/2210:10
 */
public class JwtAuthFilter implements WebFilter {

    /**
     * 通过该过滤器校验是否拥有jwt
     * 如果是登录或注册则直接放过
     * 如果不是登陆或注册则进行拦截校验
     * jwt为空或开头不合法则返回失败
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getPath().value();
        if (path.contains("/login") || path.contains("/signup")) return chain.filter(exchange);

        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null) {
            return this.writeErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "没有携带token");
        }
        else if (!auth.startsWith("Bearer ")) {
            return this.writeErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "token无效");
        }

        String token = auth.replace("Bearer ","");
        try {
            exchange.getAttributes().put("token", token);
        } catch (Exception e) {
            return this.writeErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return chain.filter(exchange);
    }

    private Mono<Void> writeErrorMessage(ServerHttpResponse response, HttpStatus httpStatus, String msg) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"status\":"+ httpStatus + ",\"msg\":"+msg+"}";
        DataBuffer dataBuffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }
}
