package org.pencil.filter.global;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.pencil.entity.dto.User;
import org.pencil.exception.GatewayException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器,鉴权处理,token校验
 * @author pencil
 * @Date 24/08/08
 */
@Slf4j
@Component
public class FirstGatewayFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    public FirstGatewayFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:8080").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        boolean skip = Boolean.TRUE.equals(exchange.getAttribute("self-skip"));
        if (skip) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 从Cookie中提取token
        HttpCookie token = extractTokenFromCookie(request);

        if (token == null || CharSequenceUtil.isBlank(token.getValue())) {
            return unauthorized(response);
        }

        // 异步调用其他服务鉴权,包括其他的各种异常场景处理
        // 处理运行时异常
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/demo/auth")
                        .queryParam("token", token.getValue())
                        .build())
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(ex -> Mono.error(GatewayException.of("Token invalid")))
                .flatMap(user -> {
                    if (user == null || CharSequenceUtil.isBlank(user.getId())) {
                        return Mono.error(GatewayException.of("Invalid token"));
                    } else {
                        // 将 userId 放入请求头
                        ServerHttpRequest mutatedRequest = request.mutate()
                                .header("X-User-Id", user.getId())
                                .header("X-User-Name", user.getName())
                                .header("X-User-Expire", String.valueOf(user.getExpire()))
                                .build();
                        ServerWebExchange exchange1 = exchange.mutate().request(mutatedRequest).build();

                        return chain.filter(exchange1);
                    }
                })
                .then(chain.filter(exchange));
    }

    private HttpCookie extractTokenFromCookie(ServerHttpRequest request) {
        return request.getCookies().getFirst("token");
    }

    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
