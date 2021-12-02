package com.melon.gateway.filter;

import com.melon.auth.JwtUtils;
import com.melon.gateway.handler.HttpResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JWTAuthorizationFilter implements GlobalFilter, Ordered {

    private String tokenHeader = "Authorization";

    @Autowired
    private JwtUtils jwtUtils;

    private static final String[] ignoringPath = new String[]{
        "/doc.html",
        "/webjars",
        "/swagger-resources",
        "/v2/api-docs",
        "/fdfs",
        "/user/login",
        "/user/register",
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();

        // 直接放行路由
        for (String str : ignoringPath) {
            if (path.indexOf(str) >= 0) {
                // 放行
                return chain.filter(exchange);
            }
        }

        String token = request.getHeaders().getFirst(tokenHeader);
        // token 不存在
        if (!StringUtils.isEmpty(token) &&
                jwtUtils.validateToken(token)) {
            // 解析 token
            String userId = jwtUtils.getUserIdFromToken(token);
            if (!StringUtils.isEmpty(userId)) {
                // 将 userId 放入请求头中供后续使用
                ServerHttpRequest newRequest = request.mutate().header("userId", userId).build();
                ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
                // 放行
                return chain.filter(newExchange);
            }
        }

        return HttpResponseHandler.customResponse(exchange, 403, "用户未登录", null);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
