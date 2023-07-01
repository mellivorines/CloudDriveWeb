package io.github.mellivorines.clouddriveweb.cache.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @Description:Redis工具类
 * 目前只用到String 只提供String的方法
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
/**
 * Redis工具类
 * 目前只用到String 只提供String的方法
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Component(value = "redisUtil")
@ConditionalOnProperty(prefix = "rpan.cache", name = ["type"], havingValue = "com.rubin.rpan.cache.redis.RedisCache")
class RedisUtil {
    @Autowired
    @Qualifier(value = "redisTemplate")
    private lateinit var redisTemplate: RedisTemplate<Any, Any>

    /**
     * 存储键值对
     *
     * @param key
     * @param value
     */
    operator fun set(key: String, value: Any) {
        redisTemplate.opsForValue().set(key, value)
    }

    /**
     * 存储键值对
     *
     * @param key
     * @param value
     * @param timeout
     */
    operator fun set(key: String, value: Any, timeout: Long) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS)
    }

    /**
     * 获取value
     *
     * @param key
     * @return
     */
    operator fun get(key: String?): Any? {
        return redisTemplate.opsForValue()[key!!]
    }

    /**
     * 删除键值对
     *
     * @param keys
     * @return
     */
    fun del(vararg keys: String): Boolean {
        return if (keys.isNotEmpty()) {
            if (keys.size == 1) {
                redisTemplate.delete(keys[0])
            } else {
                redisTemplate.delete(Arrays.asList(*keys) as Collection<Any>).toInt() == keys.size
            }
        } else true
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param expire
     */
    fun expire(key: String, expire: Long) {
        redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS)
    }
}
