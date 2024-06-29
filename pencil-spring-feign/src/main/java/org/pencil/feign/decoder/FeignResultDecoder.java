package org.pencil.feign.decoder;

import feign.*;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.pencil.constant.Constant;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author pencil
 * @Date 24/06/29
 */
@Slf4j
public class FeignResultDecoder implements Decoder {

    private final SpringDecoder springDecoder;
    public FeignResultDecoder(SpringDecoder springDecoder) {
        this.springDecoder = springDecoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        String responseStr = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

        if (log.isDebugEnabled()) {
            RequestTemplate requestTemplate = response.request().requestTemplate();
            long endTime = System.currentTimeMillis();
            long startTime = Long.parseLong(requestTemplate.headers().get(Constant.X_REQUEST_START_TIME).iterator().next());
            log.debug("Feign请求调用\n请求url={}\n请求耗时={}ms\n请求参数={}\n请求body={}\n请求header={}\n响应结果={}",
                    requestTemplate.path(),endTime - startTime, requestTemplate.queries(),
                    Optional.ofNullable(requestTemplate.body()).map(String::new).orElse(null),
                    requestTemplate.headers(),responseStr);
        }

        return springDecoder.decode(response.toBuilder().body(responseStr.getBytes()).build(), type);
    }
}
