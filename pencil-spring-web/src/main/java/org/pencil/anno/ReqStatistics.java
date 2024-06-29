package org.pencil.anno;

import java.lang.annotation.*;

/**
 * @author pencil
 * @Date 24/05/06
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqStatistics {

    String value() default "";

    boolean req() default false;

    boolean resp() default false;

    boolean costTime() default false;
}
