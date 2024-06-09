package com.jay.sales.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Value("\${spring.redis.host}")
    private lateinit var host: String

    @Value("\${spring.redis.port}")
    private var port: Int = 0

    @Bean
    fun connectionFactory() = LettuceConnectionFactory(host, port)

    @Bean
    fun redisTemplate() = RedisTemplate<String, Any>().apply {
        this.connectionFactory = connectionFactory()
        this.keySerializer = StringRedisSerializer()
        this.valueSerializer = GenericJackson2JsonRedisSerializer()
    }

}
