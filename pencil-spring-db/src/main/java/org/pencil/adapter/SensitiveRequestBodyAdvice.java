package org.pencil.adapter;

import lombok.extern.slf4j.Slf4j;
import mybatis.mate.params.IParamsProcessor;
import org.pencil.config.sensitive.Sensitivity;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author pencil
 * @Date 24/07/19
 */
@Slf4j
@RestControllerAdvice
public class SensitiveRequestBodyAdvice extends RequestBodyAdviceAdapter {
    private final IParamsProcessor paramsProcessor;

    public SensitiveRequestBodyAdvice(IParamsProcessor paramsProcessor) {
        this.paramsProcessor = paramsProcessor;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> clazz = (Class) targetType;
        return Sensitivity.class.isAssignableFrom(clazz);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            String content = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    paramsProcessor.execute("json", content).getBytes(StandardCharsets.UTF_8));
            return new MappingJacksonInputMessage(inputStream, inputMessage.getHeaders());
        } catch (IOException e) {
            log.error("SensitiveRequestBodyAdvice error", e);
        }
        return inputMessage;
    }
}