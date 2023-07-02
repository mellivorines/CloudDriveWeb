package io.github.mellivorines.cloud.drive.web.cache.core


/**
 * @Description:缓存顶级接口
 * 外部扩展缓存可以继承该接口并配置cloud.drive.cache.type=实现类的全限定类名 来自定义扩展
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
interface Cache {
    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    operator fun get(key: Any): Any?

    /**
     * 存入一个永久的键值对
     *
     * @param key
     * @param value
     */
    fun put(key: Any, value: Any)

    /**
     * 存入一个有过期时间的键值对
     *
     * @param key
     * @param value
     * @param expire
     */
    fun put(key: Any, value: Any, expire: Long)

    /**
     * 刷新某个key的过期时间
     *
     * @param key
     * @param expire
     */
    fun setExpire(key: Any, expire: Long)

    /**
     * 清除一个key
     *
     * @param key
     */
    fun delete(key: Any)
}

