package io.github.mellivorines.cloud.drive.web.cache.local

import io.github.mellivorines.cloud.drive.web.cache.core.Cache
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import java.util.concurrent.ConcurrentHashMap


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
/**
 * 默认Cache实现
 * 使用jvm本地内存实现，不支持数据持久化，支持过期时间自动删除
 * 由于网盘业务对于同一个key不存在同步读写，所以并发问题未深入控制
 */
@Component(value = "localCache")
@ConditionalOnProperty(prefix = "cloud.drive.web.cache", name = ["type"], havingValue = "io.github.mellivorines.cloud.drive.web.cache.local.LocalCache")
class LocalCache : Cache, Serializable {
    /**
     * 缓存池
     */
    private val CACHE_POOL: MutableMap<Any, Any> = ConcurrentHashMap()

    /**
     * key的过期池
     */
    private val EXPIRE_POOL: MutableMap<Any, Long> = ConcurrentHashMap()

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    override operator fun get(key: Any): Any? {
        var value = CACHE_POOL[key]
        if (Objects.isNull(value)) {
            return value
        }
        val longTime = EXPIRE_POOL[key]
        if (FOREVER_SIGN != longTime) {
            val expireDate = Date(EXPIRE_POOL[key]!!)
            if (Date().after(expireDate)) {
                delete(key)
                value = null
            }
        }
        return value
    }

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    override fun put(key: Any, value: Any) {
        CACHE_POOL[key] = value
        EXPIRE_POOL[key] = FOREVER_SIGN
    }

    /**
     * 存入一个有过期时间的键值对
     *
     * @param key
     * @param value
     * @param expire
     */
    override fun put(key: Any, value: Any, expire: Long) {
        CACHE_POOL[key] = value
        setExpire(key, expire)
    }

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    override fun setExpire(key: Any, expire: Long) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, (expire / 1000).toInt())
        EXPIRE_POOL[key] = calendar.time.time
    }

    /**
     * 清除一个key
     *
     * @param key
     */
    override fun delete(key: Any) {
        CACHE_POOL.remove(key)
        EXPIRE_POOL.remove(key)
    }

    companion object {
        private const val serialVersionUID = 8696403508878817326L

        /**
         * 永久有效标识
         */
        private const val FOREVER_SIGN = -1L
    }
}
