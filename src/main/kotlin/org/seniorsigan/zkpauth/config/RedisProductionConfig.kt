package org.seniorsigan.zkpauth.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import redis.clients.jedis.Protocol
import java.net.URI

@Configuration
open class RedisProductionConfig {
    @Value("\${redisURL}")
    lateinit var redisURL: String

    @Bean
    open fun jedisConnectionFactory(): JedisConnectionFactory {
        val uri = URI(redisURL)
        return with(JedisConnectionFactory(), {
            usePool = true
            hostName = uri.host
            port = uri.port
            timeout = Protocol.DEFAULT_TIMEOUT
            password = uri.userInfo?.split(":")?.getOrNull(1)
            this
        })
    }
}
