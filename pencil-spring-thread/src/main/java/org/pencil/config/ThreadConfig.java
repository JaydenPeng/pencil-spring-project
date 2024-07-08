package org.pencil.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author pencil
 * @Date 24/07/08
 */
public class ThreadConfig {

    @Value("${pencil.thread.corePoolSize:10}")
    private int corePoolSize;

    @Value("${pencil.thread.maxPoolSize:20}")
    private int maxPoolSize;

    @Value("${pencil.thread.queueCapacity:200}")
    private int queueCapacity;

    @Value("${pencil.thread.keepAliveSeconds:60}")
    private int keepAliveSeconds;

    @Value("${pencil.thread.threadNamePrefix:Pencil-}")
    private String threadNamePrefix;


    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置线程名称前缀
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
