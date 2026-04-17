package com.bank.publicinfo.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        final CachingProvider provider = Caching.getCachingProvider();
        final javax.cache.CacheManager jcacheManager = provider.getCacheManager();

        final CacheConfiguration<String, Object> transferConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, Object.class,
                        ResourcePoolsBuilder.heap(1000))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                        Duration.ofDays(15)))
                .build();

        final CacheConfiguration<String, Object> antifraudConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, Object.class,
                        ResourcePoolsBuilder.heap(500))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                        Duration.ofDays(365)))
                .build();

        jcacheManager.createCache("transfer",
                Eh107Configuration.fromEhcacheCacheConfiguration(transferConfig));
        jcacheManager.createCache("antifraud",
                Eh107Configuration.fromEhcacheCacheConfiguration(antifraudConfig));

        return new JCacheCacheManager(jcacheManager);
    }
}
