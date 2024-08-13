package org.pencil.filter.decorator;

import org.pencil.entity.resp.Result;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author pencil
 * @Date 24/08/10
 */
public class ResponseDecorator extends ServerHttpResponseDecorator {

    private final ServerHttpRequest request;

    public ResponseDecorator(ServerHttpResponse delegate, ServerHttpRequest request) {
        super(delegate);
        this.request = request;
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders responseHeaders = super.getHeaders();
        HttpHeaders requestHeaders = request.getHeaders();

        // 将请求头中的指定字段复制到响应头中
        List<String> headersToCopy = List.of("X-User-Id", "X-Trace-Id", "Cookie");
        headersToCopy.forEach(header -> {
            if (!responseHeaders.containsKey(header) && requestHeaders.containsKey(header)) {
                responseHeaders.add(header, requestHeaders.getFirst(header));
            }
        });

        return responseHeaders;
    }

    /**
     * 修改响应体
     *
     * @param body 响应体的 Publisher
     * @return Mono<Void> 完成的 Mono
     */
    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        // 将 Publisher<DataBuffer> 转换为 Flux<DataBuffer>
        Flux<DataBuffer> fluxBody = Flux.from(body);

        // 使用 DataBufferUtils.join 来读取响应体
        return DataBufferUtils.join(fluxBody)
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    String responseBody = new String(bytes, StandardCharsets.UTF_8);

                    // 根据响应状态码进行修改
                    if (getDelegate().getStatusCode() == null || getDelegate().getStatusCode().isError()) {
                        // 修改错误响应体
                        String modifiedResponseBody = Result.error(-100, "error").toJson();
                        DataBuffer newBuffer = getDelegate().bufferFactory().wrap(modifiedResponseBody.getBytes(StandardCharsets.UTF_8));
                        return super.writeWith(Flux.just(newBuffer));
                    } else {
                        // 返回原始响应体
                        DataBuffer originalBuffer = getDelegate().bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
                        return super.writeWith(Flux.just(originalBuffer));
                    }
                });
    }
}
