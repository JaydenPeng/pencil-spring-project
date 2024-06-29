package org.pencil.test.beans.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author pencil
 * @Date 24/06/29
 */
@Data
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
