package org.pencil.context;

import org.pencil.entity.ComContext;

/**
 * @author pencil
 * @Date 24/07/15
 */
public class RequestContext {

    private static final ThreadLocal<ComContext> contextHolder = new ThreadLocal<>();

    public static ComContext get() {
        return contextHolder.get();
    }

    public static void set(ComContext context) {
        contextHolder.set(context);
    }

    public static void clear() {
        contextHolder.remove();
    }

}
