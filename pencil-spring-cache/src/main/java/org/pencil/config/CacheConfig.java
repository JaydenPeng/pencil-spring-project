package org.pencil.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author pencil
 * @Date 24/07/02
 */
@EnableCaching
public class CacheConfig {

    @Value("${pencil.cache.spring.expire:10}")
    private int springCacheExpire;

    @Value("${pencil.cache.spring.size:10000}")
    private int springCacheSize;

    @Value("${pencil.cache.caffeine.expire:10}")
    private int caffeineExpire;

    @Value("${pencil.cache.caffeine.size:10000}")
    private int caffeineSize;

    /**
     * caffeine缓存管理器
     * 为Cacheable注解来注入bean的
     * @return
     */
    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(springCacheExpire, TimeUnit.MINUTES)
                .maximumSize(springCacheSize));
        return cacheManager;
    }

    @Bean
    public Cache<String, String> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(caffeineExpire, TimeUnit.MINUTES)
                .maximumSize(caffeineSize)
                .build();
    }

}
