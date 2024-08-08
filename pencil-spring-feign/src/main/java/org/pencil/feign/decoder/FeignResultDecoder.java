package org.pencil.feign.decoder;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import feign.*;
import feign.codec.Decoder;
import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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
    private final Counter feignCounter;

    public FeignResultDecoder(SpringDecoder springDecoder, Counter feignCounter) {
        this.springDecoder = springDecoder;
        this.feignCounter = feignCounter;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        String responseStr = Strings.EMPTY;
        if (response.body() != null) {
            responseStr = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
        }

        if (log.isDebugEnabled()) {
            RequestTemplate requestTemplate = response.request().requestTemplate();
            long endTime = System.currentTimeMillis();
            long startTime = Long.parseLong(requestTemplate.headers().get(Constant.X_REQUEST_START_TIME).iterator().next());
            log.debug("Feign请求调用\n请求url={}\n请求耗时={}ms\n请求参数={}\n请求body={}\n请求header={}\n响应结果={}",
                    requestTemplate.path(),endTime - startTime, requestTemplate.queries(),
                    Optional.ofNullable(requestTemplate.body()).map(String::new).orElse(null),
                    requestTemplate.headers(), CharSequenceUtil.sub(responseStr, 0, 1024));

            try {
                feignCounter.labels(requestTemplate.path(), String.valueOf(response.status())).inc();
            } catch (Exception e) {
                log.error("FeignResultDecoder feignCounter error", e);
            }
            
        }

        return springDecoder.decode(response.toBuilder().body(responseStr.getBytes()).build(), type);
    }
}
