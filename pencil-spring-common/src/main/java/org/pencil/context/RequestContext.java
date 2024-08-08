package org.pencil.context;

import org.pencil.entity.ComContext;

/**
 * @author pencil
 * @Date 24/07/15
 */
public class RequestContext {

    private static final ThreadLocal<ComContext> CONTEXT_HOLDER = new ThreadLocal<>();

    public static ComContext get() {
        return CONTEXT_HOLDER.get();
    }

    public static void set(ComContext context) {
        CONTEXT_HOLDER.set(context);
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    private RequestContext() {}

}
