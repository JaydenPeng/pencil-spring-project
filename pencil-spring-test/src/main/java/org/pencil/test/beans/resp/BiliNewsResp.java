package org.pencil.test.beans.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pencil
 * @Date 24/06/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiliNewsResp {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("time")
    private String time;
    @JsonProperty("data")
    private List<DataDTO> data;

}
