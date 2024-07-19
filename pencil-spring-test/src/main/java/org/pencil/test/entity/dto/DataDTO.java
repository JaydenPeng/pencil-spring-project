package org.pencil.test.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author pencil
 * @Date 24/06/29
 */
@Data
public class DataDTO {
    @JsonProperty("title")
    private String title;
    @JsonProperty("heat")
    private String heat;
    @JsonProperty("link")
    private String link;
}
