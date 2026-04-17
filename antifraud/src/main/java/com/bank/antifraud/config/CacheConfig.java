package com.bank.antifraud.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.slf4j.Logger;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@EnableCaching
@Configuration
public class CacheConfig {
    
    private final static Long TIME_TO_LIVE_MINUTES = 15L;
    private final static Long MAX_ENTRIES = 500L;
    
    private final Logger logger;
    
    public CacheConfig(Logger logger) {
        this.logger = logger;
    }
    
    @Bean
    public javax.cache.CacheManager cacheManager() {
        final EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider();
        final CacheManager cacheManager = provider.getCacheManager();
        
        final org.ehcache.config.CacheConfiguration<Long, Object> ehcacheConfig =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    Long.class,
                    Object.class,
                    ResourcePoolsBuilder.heap(MAX_ENTRIES)
                )
                .withExpiry(
                    ExpiryPolicyBuilder.timeToLiveExpiration(Duration.of(TIME_TO_LIVE_MINUTES, ChronoUnit.MINUTES)
                ))
                .build();
        
        final javax.cache.configuration.Configuration<Long, Object> jcacheConfig =
                Eh107Configuration.fromEhcacheCacheConfiguration(ehcacheConfig);
        
        createCacheIfAbsent(cacheManager, "externalAccountTransferCache", jcacheConfig);
        createCacheIfAbsent(cacheManager, "externalCardTransferCache", jcacheConfig);
        createCacheIfAbsent(cacheManager, "externalPhoneTransferCache", jcacheConfig);
        
        return cacheManager;
    }
    
    @Bean("feignTransferCacheManager")
    public org.springframework.cache.CacheManager springCacheManager(javax.cache.CacheManager jcacheCacheManager) {
        return new JCacheCacheManager(jcacheCacheManager);
    }
    
    private void createCacheIfAbsent(javax.cache.CacheManager cacheManager,
                                     String name,
                                     javax.cache.configuration.Configuration<Long, Object> config) {
        if (cacheManager.getCache(name) == null) {
            cacheManager.createCache(name, config);
            logger.info("Кэш '{}' создан", name);
        } else {
            logger.info("Кэш '{}' уже существует", name);
        }
    }
}
