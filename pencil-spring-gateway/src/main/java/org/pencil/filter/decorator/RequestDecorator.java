package org.pencil.filter.decorator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;

/**
 * @author pencil
 * @Date 24/08/10
 */
public class RequestDecorator extends ServerHttpRequestDecorator {

    private final ServerHttpRequest delegate;

    public RequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public Builder mutate() {
        return super.mutate();
    }
}
