package org.pencil.handler;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.pencil.entity.resp.Result;
import org.pencil.exception.GatewayException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 全局异常处理
 *
 * @author pencil
 * @Date 24 /08/09
 */
@Slf4j
@Component
@Order(-1)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpRequest request = exchange.getRequest();

        log.error("GlobalExceptionHandler: {} {}", request.getMethod(), request.getURI(), ex);

        if (ex instanceof WebClientResponseException webClientException) {
            // 如果是服务端抛出的异常，直接抛给调用方，不做处理
            exchange.getResponse().setStatusCode(webClientException.getStatusCode());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(webClientException.getResponseBodyAsByteArray())));
        } else {
            // 网关内部异常处理
            HttpStatus status;
            Result<Void> errorResponse;

            if (ex instanceof GatewayException) {
                // or any other appropriate status code
                status = HttpStatus.BAD_REQUEST;
                errorResponse = Result.error(-100, ex.getMessage());
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorResponse = Result.error(-500, "An unexpected error occurred");
            }

            return buildErrorResponse(errorResponse, exchange, status);
        }
    }

    private Mono<Void> buildErrorResponse(Result<Void> errorResponse, ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(JSONUtil.toJsonStr(errorResponse).getBytes())));
    }

}
