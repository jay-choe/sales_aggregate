package com.jay.sales.infrastructure.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration
import java.util.concurrent.TimeUnit


@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    @Profile("local-cache-strategy")
    fun localCacheManager(): CacheManager {
        return CaffeineCacheManager("min-price-products").apply {
            this.setCaffeine(caffeineCache())
        }
    }

    @Bean
    @Profile("global-cache-strategy")
    fun globalCacheManger(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(3))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))

        return RedisCacheManager.builder(redisConnectionFactory).initialCacheNames(setOf("min-price-products"))
            .cacheDefaults(redisCacheConfig)
            .build()
    }

    private fun caffeineCache(): Caffeine<Any, Any> {
        return Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .maximumSize(100L)
    }
}
