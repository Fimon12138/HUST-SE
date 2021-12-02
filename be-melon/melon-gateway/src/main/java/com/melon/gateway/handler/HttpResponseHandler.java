package com.melon.gateway.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class HttpResponseHandler {

    public static Mono<Void> customResponse(ServerWebExchange exchange,
                             Integer code, String message, Object object) {
        ServerHttpResponse response = exchange.getResponse();

        JSONObject res = new JSONObject();
        res.put("code", code);
        res.put("message", message);
        res.put("result", object);

        byte[] bits = res.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);

        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));

    }

}
