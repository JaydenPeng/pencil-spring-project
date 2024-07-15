package org.pencil.entity;

import lombok.Data;

/**
 * @author pencil
 * @Date 24/07/15
 */
@Data
public class ComContext {

    private String traceId;

    private String userId;

    private String requestIp;

}
