package com.bank.transfer.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import lombok.extern.slf4j.Slf4j;
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
        final EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider();
        final CacheManager cacheManager = provider.getCacheManager();

        final org.ehcache.config.CacheConfiguration<Long, Object> ehcacheConfig =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class,
                                Object.class,
                                ResourcePoolsBuilder.heap(100))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofDays(5)))
                        .build();

        final javax.cache.configuration.Configuration<Long, Object> jcacheConfig =
                Eh107Configuration.fromEhcacheCacheConfiguration(ehcacheConfig);

        cacheManager.createCache("profileAccountDetailsCache", jcacheConfig);
        log.info("Кэш 'profileAccountDetailsCache' создан с временем жизни 5 дней");

        return cacheManager;
    }
}
