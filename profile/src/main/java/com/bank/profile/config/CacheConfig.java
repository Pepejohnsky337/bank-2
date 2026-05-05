package com.bank.profile.config;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.time.Duration;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager ehCacheManager() {

        final EhcacheCachingProvider ehcacheCachingProvider = (EhcacheCachingProvider) Caching.getCachingProvider();
        final CacheManager cacheManager = ehcacheCachingProvider.getCacheManager();

        final org.ehcache.config.CacheConfiguration<Long, Object> cacheConfiguration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class,
                                Object.class,
                                ResourcePoolsBuilder.heap(100))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(5)))
                        .build();

        final javax.cache.configuration.Configuration<Long, Object> javaxCacheConfiguration =
                Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);

        cacheManager.createCache("accountDetailsCache", javaxCacheConfiguration);
        log.info("EhcacheCachingProvider created");
        return cacheManager;
    }
}
