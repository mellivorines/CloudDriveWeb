package io.github.mellivorines.cloud.drive.web.utils

import java.util.*


/**
 * @Description:分享ID工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object ShareIdUtil {
    private const val ZERO_LONG = 0L
    private val threadLocal = ThreadLocal<Long>()

    /**
     * 设置当前查看的分享ID
     *
     * @param value
     */
    fun set(value: Long) {
        threadLocal.set(value)
    }

    /**
     * 获取当前查看的分享ID
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
