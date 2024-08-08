package org.pencil.filter.custom;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * 自定义过滤器,可以给特定url定制化
 * @author pencil
 * @Date 24/08/09
 */
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 自定义过滤逻辑
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r.headers(headers -> headers.add(HttpHeaders.AUTHORIZATION, config.getAuthToken())))
                    .build();
            return chain.filter(modifiedExchange);
        };
    }


    public static class Config {
        // 定义过滤器所需的配置参数
        private String authToken;

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }
    }

}
