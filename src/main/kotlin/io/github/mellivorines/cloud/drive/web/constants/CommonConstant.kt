package io.github.mellivorines.cloud.drive.web.constants

import java.io.File


/**
 * @Description:公用常量类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
object CommonConstant {
    const val COMMON_SEPARATOR = "__,__"
    const val EMPTY_STR = ""
    const val POINT_STR = "."
    const val SLASH_STR = "/"
    const val ZERO_LONG = 0L
    const val ZERO_INT = 0
    const val ONE_INT = 1
    const val TWO_INT = 2
    const val SEVEN_INT = 7
    const val MINUS_ONE_INT = -1
    const val TRUE_STR = "true"
    const val ONE_DAY_LONG = 24L * 60L * 60L * 1000L
    const val FIVE_MINUTES_LONG = 5L * 60L * 1000L
    const val ONE_HOUR_LONG = 60L * 60L * 1000L
    const val DEFAULT_SHARE_PREFIX = "http://127.0.0.1:7001/share/"

    /**
     * 登录用户ID的key
     */
    const val LOGIN_USER_ID = "LOGIN_USER_ID"

    /**
     * 分享ID
     */
    const val SHARE_ID = "SHARE_ID"

    /**
     * 用户登录后存储redis的key前缀
     */
    const val USER_LOGIN_PREFIX = "USER_LOGIN_"

    const val USERNAME_ENCRYPTION_STR = "****"
    const val USER_FORGET_PREFIX = "USER_FORGET_"

    const val CONTENT_TYPE_STR = "content-type"
    const val APPLICATION_OCTET_STREAM_STR = "application/octet-stream"
    const val CONTENT_DISPOSITION_STR = "Content-Disposition"
    const val CONTENT_DISPOSITION_VALUE_PREFIX_STR = "attachment;fileName="
    const val GB2312_STR = "GB2312"
    const val IOS_8859_1_STR = "ISO-8859-1"
    val SEPARATOR_STR: String = File.separator
    const val CN_LEFT_PARENTHESES_STR = "（"
    const val CN_RIGHT_PARENTHESES_STR = "）"
    const val ALL_FILE_CN_STR = "全部文件"

    /**
     * 所有文件类型查询标识
     */
    const val ALL_FILE_TYPE = "-1"

    /**
     * 是否是文件夹枚举类
     */
    enum class FolderFlagEnum(val code: Int) {
        NO(0),
        YES(1)

    }


    /**
     * 是否删除
     */
    enum class DelFlagEnum(val code: Int) {
        NO(0),
        YES(1)

    }
}

