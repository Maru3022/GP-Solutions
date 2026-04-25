package com.example.hotelapi.config;

import java.time.Duration;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return builder -> builder
                .withCacheConfiguration("hotels",
                        defaultConfig
                                .entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration("hotelDetails",
                        defaultConfig
                                .entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration("hotelSearch",
                        defaultConfig
                                .entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("hotelHistogram",
                        defaultConfig
                                .entryTtl(Duration.ofMinutes(15)));
    }

    @Bean
    public CacheManagerCustomizer<RedisCacheManager> redisCacheManagerCustomizer() {
        return cacheManager -> cacheManager.setTransactionAware(true);
    }

    @Bean
    public SimpleKeyGenerator simpleKeyGenerator() {
        return new SimpleKeyGenerator();
    }
}
