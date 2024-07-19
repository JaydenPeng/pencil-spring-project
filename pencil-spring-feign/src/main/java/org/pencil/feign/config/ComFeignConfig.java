package org.pencil.feign.config;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import io.prometheus.client.Counter;
import org.pencil.feign.decoder.FeignErrorDecoder;
import org.pencil.feign.decoder.FeignResultDecoder;
import org.pencil.feign.interceptor.ReqFeignInterceptor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author pencil
 * @Date 24/06/29
 */
public class ComFeignConfig {

    @Resource
    private Counter feignCounter;

    /**
     * NONE：不记录任何日志，默认值
     * BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
     * HEADERS：在BASIC基础上，额外记录了请求和响应的头信息
     * FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据
     */
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public Decoder feignResultDecoder(ObjectFactory<HttpMessageConverters> messageConverters){
        return new FeignResultDecoder(new SpringDecoder(messageConverters), feignCounter);
    }

    @Bean
    public ReqFeignInterceptor reqFeignInterceptor(){
        return new ReqFeignInterceptor();
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder(feignCounter);
    }

}
