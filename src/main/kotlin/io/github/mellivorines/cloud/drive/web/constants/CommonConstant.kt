package io.github.mellivorines.cloud.drive.web.constants


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
}

