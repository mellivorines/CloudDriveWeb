package io.github.mellivorines.cloud.drive.web.utils

import java.util.*


/**
 * @Description:UUID工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/30
 */
object UUIDUtil {
    private const val EMPTY_STR = ""
    private const val HYPHEN_STR = "-"
    val uUID: String
        /**
         * 获取UUID字符串
         *
         * @return
         */
        get() = UUID.randomUUID().toString().replace(HYPHEN_STR, EMPTY_STR).uppercase(Locale.getDefault())
}

