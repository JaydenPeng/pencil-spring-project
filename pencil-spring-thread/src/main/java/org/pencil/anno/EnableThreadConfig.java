package org.pencil.anno;

import org.pencil.config.ThreadConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pencil
 * @Date 24/07/08
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ThreadConfig.class)
public @interface EnableThreadConfig {
}
