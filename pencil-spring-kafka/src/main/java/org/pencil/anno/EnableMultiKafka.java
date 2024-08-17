package org.pencil.anno;

import org.pencil.config.KafkaEnableConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pencil
 * @Date 24/08/17
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(KafkaEnableConfig.class)
public @interface EnableMultiKafka {
}
