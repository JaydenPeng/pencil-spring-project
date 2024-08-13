package org.pencil.entity.resp;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pencil.constant.Constant;

/**
 * @author pencil
 * @Date 24/06/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, data);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    public String toJson() {
        return JSONUtil.toJsonStr(this);
    }

}
