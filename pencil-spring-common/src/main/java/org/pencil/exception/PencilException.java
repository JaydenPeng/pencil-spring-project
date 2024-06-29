package org.pencil.exception;

/**
 * @author pencil
 * @Date 24/06/29
 */
public class PencilException extends RuntimeException {

    public PencilException(String message) {
        super(message);
    }

    public static PencilException of(String message) {
        return new PencilException(message);
    }
}
