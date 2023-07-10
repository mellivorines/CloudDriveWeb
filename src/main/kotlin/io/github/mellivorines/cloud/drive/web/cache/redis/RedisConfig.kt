package io.github.mellivorines.cloud.drive.web.cache.redis

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.util.*


/**
 * @Description:Redis配置类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
@SpringBootConfiguration
@ConditionalOnProperty(prefix = "cloud.drive.web.cache", name = ["type"], havingValue = "io.github.mellivorines.cloud.drive.web.cache.redis.RedisCache")
@ConfigurationProperties(prefix = "cloud.drive.web.cache.redis")
class RedisConfig {
    var database = DEFAULT_DATABASE
    var host = DEFAULT_HOST
    var port = DEFAULT_PORT
    var password = EMPTY_STR

    @Bean(value = ["redisConnectionFactory"])
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()
        redisStandaloneConfiguration.database = database
        redisStandaloneConfiguration.hostName = host
        redisStandaloneConfiguration.port = port
        redisStandaloneConfiguration.password = RedisPassword.of(password)
        val lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder()
        return LettuceConnectionFactory(
            redisStandaloneConfiguration,
            lettuceClientConfigurationBuilder.build()
        )
    }

    @Bean(value = ["redisTemplate"])
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        // 配置redisTemplate
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = redisConnectionFactory
        val stringSerializer: RedisSerializer<*> = StringRedisSerializer()
        // key序列化
        redisTemplate.keySerializer = stringSerializer
        // value序列化
        redisTemplate.valueSerializer = GenericJackson2JsonRedisSerializer()
        // Hash key序列化
        redisTemplate.hashKeySerializer = stringSerializer
        // Hash value序列化
        redisTemplate.hashValueSerializer = GenericJackson2JsonRedisSerializer()
        redisTemplate.afterPropertiesSet()
        log.info("cloudDriveWeb Redis配置完毕！")
        return redisTemplate
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RedisConfig
        return database == that.database && host == that.host && port == that.port && password == that.password
    }

    override fun hashCode(): Int {
        return Objects.hash(database, host, port, password)
    }

    override fun toString(): String {
        return "RedisConfig{" +
                "database=" + database +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                '}'
    }

    companion object {
        private val log = LoggerFactory.getLogger(RedisConfig::class.java)
        private const val EMPTY_STR = ""
        private const val DEFAULT_DATABASE = 0
        private const val DEFAULT_HOST = "127.0.0.1"
        private const val DEFAULT_PORT = 6379
    }
}
