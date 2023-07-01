package io.github.mellivorines.clouddriveweb.cache.core

import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.util.*


/**
 * @Description:缓存选择器
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */

@Component(value = "cacheSelector")
class CacheSelector : ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext
    private lateinit var cache: Cache

    @Value("\${rpan.cache.type}")
    private lateinit var cacheType: Class<out Cache>


    @Throws(BeansException::class)
    override fun setApplicationContext(context: ApplicationContext) {
        this.applicationContext = context
        if (Objects.isNull(cacheType)) {
            throw RuntimeException("the cache type can not be null!<rpan.cache.type>")
        }
        cache = applicationContext.getBean(cacheType)
    }

    /**
     * 选择Cache实现类
     *
     * @return
     */
    fun select(): Cache {
        return cache
    }
}

