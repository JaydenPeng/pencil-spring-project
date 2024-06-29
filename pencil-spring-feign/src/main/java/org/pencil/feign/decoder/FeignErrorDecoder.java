package org.pencil.feign.decoder;

import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.pencil.constant.Constant;
import org.pencil.exception.PencilException;

import java.util.Optional;

/**
 * @author pencil
 * @Date 24/06/29
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        String reason = response.reason();

        RequestTemplate requestTemplate = response.request().requestTemplate();
        long endTime = System.currentTimeMillis();
        long startTime = Long.parseLong(requestTemplate.headers().get(Constant.X_REQUEST_START_TIME).iterator().next());
        log.error("Feign请求失败\n请求url={}\n错误状态={}\n请求耗时={}ms\n请求参数={}\n请求body={}\n请求header={}\n错误信息={}",
                requestTemplate.path(), response.status(), endTime - startTime, requestTemplate.queries(),
                Optional.ofNullable(requestTemplate.body()).map(String::new).orElse(null),
                requestTemplate.headers(), reason);

        return PencilException.of(reason);
    }
}
