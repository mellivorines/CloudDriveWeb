package io.github.mellivorines.cloud.drive.web.cache.redis

import io.github.mellivorines.cloud.drive.web.cache.core.Cache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
@Component(value = "redisCache")
@ConditionalOnProperty(prefix = "cloud.drive.web.cache", name = ["type"], havingValue = "io.github.mellivorines.cloud.drive.web.cache.redis.RedisCache")
class RedisCache : Cache {
    @Autowired
    @Qualifier(value = "redisUtil")
    private lateinit var redisUtil: RedisUtil

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    override operator fun get(key: Any): Any? {
        return redisUtil.get(key.toString())
    }

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    override fun put(key: Any, value: Any) {
        redisUtil[key.toString()] = value
    }

    /**
     * 存入一个有过期时间的键值对
     *
     * @param key
     * @param value
     * @param expire
     */
    override fun put(key: Any, value: Any, expire: Long) {
        redisUtil[key.toString(), value] = expire
    }

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    override fun setExpire(key: Any, expire: Long) {
        redisUtil.expire(key.toString(), expire)
    }

    /**
     * 清除一个key
     *
     * @param key
     */
    override fun delete(key: Any) {
        redisUtil.del(key.toString())
    }
}
