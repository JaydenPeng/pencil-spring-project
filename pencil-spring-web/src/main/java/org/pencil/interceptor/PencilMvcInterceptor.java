package org.pencil.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.pencil.constant.Constant;
import org.pencil.context.RequestContext;
import org.pencil.entity.ComContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * @author pencil
 * @Date 24/06/29
 */
@Slf4j
public class PencilMvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();

        ComContext comContext = new ComContext();

        if (ArrayUtil.isNotEmpty(cookies)) {
            Arrays.stream(cookies).filter(cookie -> Constant.USER_ID.equalsIgnoreCase(cookie.getName()))
                    .findFirst().ifPresent(cookie -> comContext.setUserId(cookie.getValue()));
        }

        comContext.setTraceId(Optional.ofNullable(request.getHeader(Constant.TRACE_ID)).orElse(UUID.randomUUID().toString()));

        comContext.setRequestIp(ServletUtil.getClientIP(request));

        RequestContext.set(comContext);

        if (log.isDebugEnabled()) {
            log.debug("pre handle, url={}, param={}, cookies={}", request.getRequestURL().toString(), request.getParameterMap(), cookies);
        }

        MDC.put("logUserId", comContext.getUserId());
        MDC.put("logTraceId", comContext.getTraceId());
        MDC.put("logRequestIp", comContext.getRequestIp());

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContext.clear();
        MDC.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
