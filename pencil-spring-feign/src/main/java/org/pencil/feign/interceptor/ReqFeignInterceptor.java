package org.pencil.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.pencil.constant.Constant;

/**
 * @author pencil
 * @Date 24/06/29
 */
public class ReqFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 添加请求时间
        requestTemplate.header(Constant.X_REQUEST_START_TIME, String.valueOf(System.currentTimeMillis()));
    }
}
