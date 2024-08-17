package org.pencil.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.pencil.anno.KafkaDataSource;
import org.pencil.config.KafkaDataSourceManager;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author pencil
 * @Date 24/08/17
 */
@Aspect
@RequiredArgsConstructor
public class KafkaDataSourceAspect {

    private final KafkaDataSourceManager kafkaDataSourceManager;

    @Around("@annotation(kafkaDataSource)")
    public Object switchDataSource(ProceedingJoinPoint joinPoint, KafkaDataSource kafkaDataSource) throws Throwable {
        KafkaTemplate<String, String> kafkaTemplate = kafkaDataSourceManager.getKafkaTemplate(kafkaDataSource.value());
        // 将KafkaTemplate注入到需要的地方，或者直接使用
        // 例如：可以将kafkaTemplate传递给被拦截的方法，或者通过代理类来发送消息
        return joinPoint.proceed(new Object[]{kafkaTemplate});
    }

}
