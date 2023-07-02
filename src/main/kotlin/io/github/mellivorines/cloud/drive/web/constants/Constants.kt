package io.github.mellivorines.cloud.drive.web.constants


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/9
 */
object Constants {
    const val ZERO_STR = "0"
    const val ZERO = 0
    const val ONE = 1
    const val LENGTH_30 = 30
    const val LENGTH_10 = 10
    const val LENGTH_20 = 20
    const val LENGTH_5 = 5
    const val LENGTH_15 = 15
    const val LENGTH_150 = 150
    const val LENGTH_50 = 50
    const val SESSION_KEY = "session_key"
    const val SESSION_SHARE_KEY = "session_share_key_"
    const val FILE_FOLDER_FILE = "/file/"
    const val FILE_FOLDER_TEMP = "/temp/"
    const val IMAGE_PNG_SUFFIX = ".png"
    const val TS_NAME = "index.ts"
    const val M3U8_NAME = "index.m3u8"
    const val CHECK_CODE_KEY = "check_code_key"
    const val CHECK_CODE_KEY_EMAIL = "check_code_key_email"
    const val AVATAR_SUFFIX = ".jpg"
    const val FILE_FOLDER_AVATAR_NAME = "avatar/"
    const val AVATAR_DEFAULT = "default_avatar.jpg"
    const val VIEW_OBJ_RESULT_KEY = "result"
    /**
     * redis key 相关
     */
    /**
     * 过期时间 1分钟
     */
    private const val REDIS_KEY_EXPIRES_ONE_MIN = 60

    /**
     * 过期时间 1天
     */
    const val REDIS_KEY_EXPIRES_DAY: Int = REDIS_KEY_EXPIRES_ONE_MIN * 60 * 24
    const val REDIS_KEY_EXPIRES_ONE_HOUR: Int = REDIS_KEY_EXPIRES_ONE_MIN * 60
    const val MB = 1024 * 1024L

    /**
     * 过期时间5分钟
     */
    val REDIS_KEY_EXPIRES_FIVE_MIN: Int = REDIS_KEY_EXPIRES_ONE_MIN * 5
    const val REDIS_KEY_DOWNLOAD = "clouddrive:download:"
    const val REDIS_KEY_SYS_SETTING = "clouddrive:syssetting:"
    const val REDIS_KEY_USER_SPACE_USE = "clouddrive:user:spaceuse:"
    const val REDIS_KEY_USER_FILE_TEMP_SIZE = "clouddrive:user:file:temp:"
}
