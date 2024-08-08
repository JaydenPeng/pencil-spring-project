package org.pencil.exception;

/**
 * @author pencil
 * @Date 24/08/09
 */
public class GatewayException extends RuntimeException {

    public GatewayException(String message) {
        super(message);
    }

    public static GatewayException of(String message) {
        return new GatewayException(message);
    }
}
