package io.github.mellivorines.cloud.drive.web.utils

import java.util.*


/**
 * @Description:用户ID工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/30
 */
object UserIdUtil {
    const val ZERO_LONG = 0L
    private val threadLocal = ThreadLocal<Long>()

    /**
     * 设置当前登录的用户ID
     *
     * @param value
     */
    fun set(value: Long) {
        threadLocal.set(value)
    }

    /**
     * 获取当前登录的用户ID
     *
     * @return
     */
    fun get(): Long {
        val value = threadLocal.get()
        return if (Objects.isNull(value)) {
            ZERO_LONG
        } else value
    }
}
