package org.pencil.config;

import org.pencil.interceptor.PencilMvcInterceptor;
import org.pencil.interceptor.ReqStatisticsInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pencil
 * @Date 24/06/29
 */
public class WebMvcConfig implements WebMvcConfigurer {

    public static final String TRACE_ANNOTATION_EXECUTION = "@annotation(org.pencil.anno.ReqStatistics)";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PencilMvcInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/static/**");
    }

    @Bean
    public ReqStatisticsInterceptor reqStatisticsInterceptor() {
        return new ReqStatisticsInterceptor();
    }

    @Bean
    public DefaultPointcutAdvisor interceptor(ReqStatisticsInterceptor reqStatisticsInterceptor) {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(TRACE_ANNOTATION_EXECUTION);

        return new DefaultPointcutAdvisor(pointcut, reqStatisticsInterceptor);
    }

}
