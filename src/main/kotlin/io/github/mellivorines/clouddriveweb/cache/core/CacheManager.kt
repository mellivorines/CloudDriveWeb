package io.github.mellivorines.clouddriveweb.cache.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component


/**
 * @Description:缓存管理器
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
@Component(value = "cacheManager")
class CacheManager : Cache {

    @Autowired
    @Qualifier(value = "cacheSelector")
    private lateinit var  cacheSelector: CacheSelector

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    override fun get(key: Any): Any? {
        return cacheSelector.select()[key]
    }

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    override fun put(key: Any, value: Any) {
        cacheSelector.select().put(key, value)
    }

    /**
     * 存入一个有过期时间的键值对
     *
     * @param key
     * @param value
     * @param expire
     */
    override fun put(key: Any, value: Any, expire: Long) {
        cacheSelector.select().put(key, value, expire)
    }

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    override fun setExpire(key: Any, expire: Long) {
        cacheSelector.select().setExpire(key, expire)
    }

    /**
     * 清除一个key
     *
     * @param key
     */
    override fun delete(key: Any) {
        cacheSelector.select().delete(key)
    }
}
