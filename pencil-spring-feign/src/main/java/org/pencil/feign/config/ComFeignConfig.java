package org.pencil.feign.config;

import feign.Logger;
import feign.Retryer;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author pencil
 * @Date 24/06/29
 */
public class ComFeignConfig {

    @Resource
    private Counter feignCounter;

    @Resource
    private Histogram feignHistogram;

    @Bean
    public Retryer retryer() {
        /*
         * period：初始重试间隔时间，单位毫秒
         * maxPeriod：最大重试间隔时间，单位毫秒
         * maxAttempts：最大重试次数
         */
        return new Retryer.Default(100, 1000, 3);
    }


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

    /**
     * 自定义Feign日志记录器
     * @return Feign日志记录器
     */
    @Bean
    public Logger getFeignLogger() {
        return new ComFeignLogger(feignCounter, feignHistogram);
    }

    /* 解码器
    @Bean
    public Decoder feignResultDecoder(ObjectFactory<HttpMessageConverters> messageConverters,
                                      ObjectProvider<HttpMessageConverterCustomizer> customizers){
        return new FeignResultDecoder(new SpringDecoder(messageConverters, customizers), feignCounter);
    }

    @Bean
    public ReqFeignInterceptor reqFeignInterceptor(){
        return new ReqFeignInterceptor();
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder(feignCounter);
    }
    */

}
