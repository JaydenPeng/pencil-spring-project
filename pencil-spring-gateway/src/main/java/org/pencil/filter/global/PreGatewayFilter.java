package org.pencil.filter.global;

import cn.hutool.core.text.CharSequenceUtil;
import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pencil.constant.Constant;
import org.pencil.filter.decorator.ResponseDecorator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局过滤器,最先过滤
 *
 * @author pencil
 * @Date 24/08/08
 */
@Slf4j
@Component
@AllArgsConstructor
public class PreGatewayFilter implements GlobalFilter, Ordered {

    private final Summary responseTimeSummary;

    private final Counter requestCounter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String routeUri = Optional.ofNullable(route).map(obj -> obj.getUri().toString()).orElse("Unknown URI");

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        HttpHeaders headers = HttpHeaders.writableHttpHeaders(request.getHeaders());
        Map<String, String> headersToUpdate = new ConcurrentHashMap<>();
        if (CharSequenceUtil.isBlank(headers.getFirst(Constant.TRACE_ID))) {
            headersToUpdate.put(Constant.TRACE_ID, headers.getFirst(Constant.TRACE_ID));
        }
        exchange.getAttributes().put(Constant.HEADERS_TO_UPDATE, headersToUpdate);

        String skip = headers.getFirst(Constant.SELF_SKIP);
        if (Constant.OK.equals(skip)) {
            exchange.getAttributes().put(Constant.GATEWAY_SKIP, true);
        }

        // 创建新的 ServerHttpRequest，包含更新后的请求头
        ResponseDecorator responseDecorator = new ResponseDecorator(response, request);

        return chain.filter(exchange.mutate().response(responseDecorator).build())
                .onErrorResume(ex -> {
                    // 解决filter中异常,仍然能将中间的头返回给调用方
                    Map<String, String> headerMap = exchange.getAttribute(Constant.HEADERS_TO_UPDATE);

                    if (headerMap != null) {
                        headerMap.forEach((key, value) -> responseDecorator.getHeaders().add(key, value));
                    }

                    return Mono.error(ex);
                })
                .then(Mono.fromRunnable(() -> {
                    long time = System.currentTimeMillis() - startTime;
                    responseTimeSummary.labels(routeUri).observe(time);
                    int statusCode = Objects.requireNonNull(responseDecorator.getStatusCode()).value();
                    requestCounter.labels(routeUri, String.valueOf(statusCode)).inc();
                }));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
