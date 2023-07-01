package io.github.mellivorines.clouddriveweb.utils

import org.apache.commons.lang3.RandomStringUtils
import java.util.*


/**
 * @Description:分享码生成器
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object ShareCodeUtil {
    /**
     * 分享码长度
     */
    private const val SHARE_CODE_LENGTH = 4

    /**
     * 生成分享码
     *
     * @return
     */
    fun get(): String {
        return RandomStringUtils.randomAlphanumeric(SHARE_CODE_LENGTH).lowercase(Locale.getDefault())
    }
}
