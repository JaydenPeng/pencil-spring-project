package org.pencil.config.decorator;

import org.pencil.context.RequestContext;
import org.pencil.entity.ComContext;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author pencil
 * @Date 24/08/01
 */
public class PencilTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {

        // 解决多线程下，requestContext丢失的问题
        ComContext comContext = RequestContext.get();
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

        return () -> {
            RequestContext.set(comContext);
            MDC.setContextMap(copyOfContextMap);
            runnable.run();
        };
    }
}
