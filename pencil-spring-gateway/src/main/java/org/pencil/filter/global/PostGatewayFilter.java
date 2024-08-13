package org.pencil.filter.global;

import lombok.extern.slf4j.Slf4j;
import org.pencil.constant.Constant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author pencil
 * @Date 24/08/10
 */
@Slf4j
@Component
public class PostGatewayFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取先前存储在 exchange 属性中的头信息
        Map<String, String> headersToUpdate = exchange.getAttribute(Constant.HEADERS_TO_UPDATE);

        if (headersToUpdate != null) {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpRequest updatedRequest = request.mutate()
                    .headers(httpHeaders -> headersToUpdate.forEach(httpHeaders::add))
                    .build();

            ServerWebExchange updatedExchange = exchange.mutate().request(updatedRequest).build();
            return chain.filter(updatedExchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
