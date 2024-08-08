package org.pencil.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pencil
 * @Date 24/08/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    /**
     * 用户名
     */
    private String name;

    /**
     * 用户id
     */
    private String id;

    /**
     * token过期时间
     */
    private long expire;
}
