package org.pencil.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.pencil.anno.ReqStatistics;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author pencil
 * @Date 24/05/06
 */
@Slf4j
public class ReqStatisticsInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // 获取方法对象
        Method method = invocation.getMethod();

        // 检查方法上是否有@LogExecution注解
        if (!method.isAnnotationPresent(ReqStatistics.class)) {
            // 如果没有注解，直接调用原方法
            return invocation.proceed();
        }

        ReqStatistics reqStatistics = method.getAnnotation(ReqStatistics.class);

        long startTime = System.currentTimeMillis();
        List<Object> objectList = new ArrayList<>();

        String methodName = invocation.getMethod().getName();

        StringBuilder sb = new StringBuilder(methodName + "方法请求统计:\n")
                .append("请求信息:").append(reqStatistics.value()).append("\n");
        if (reqStatistics.req()) {
            // 获取参数值
            Object[] args = invocation.getArguments();

            // 获取参数名和参数值
            Parameter[] parameters = method.getParameters();
            Map<String, Object> paramMap = new HashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                paramMap.put(parameters[i].getName(), args[i]);
            }

            sb.append("请求参数:{}\n");
            objectList.add(paramMap);
        }

        // 调用原方法
        Object result = invocation.proceed();

        if (reqStatistics.resp()) {
            sb.append("响应结果:{}\n");
            objectList.add(result);
        }

        if (reqStatistics.costTime()) {
            sb.append("耗时:{}ms\n");
            objectList.add(System.currentTimeMillis() - startTime);
        }

        log.info(sb.toString(), objectList.toArray());

        return result;

    }


}
