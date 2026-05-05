package com.bank.account.config;

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

        final CacheConfiguration<Long, Object> profileConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Long.class, Object.class,
                        ResourcePoolsBuilder.heap(100))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                        Duration.ofDays(5)))
                .build();

        final CacheConfiguration<Long, Object> bankDetailsConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Long.class, Object.class,
                        ResourcePoolsBuilder.heap(100))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(
                        Duration.ofDays(365)))
                .build();

        jcacheManager.createCache("profiles",
                Eh107Configuration.fromEhcacheCacheConfiguration(profileConfig));
        jcacheManager.createCache("bankDetails",
                Eh107Configuration.fromEhcacheCacheConfiguration(bankDetailsConfig));

        return new JCacheCacheManager(jcacheManager);
    }
}
