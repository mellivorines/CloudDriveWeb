package io.github.mellivorines.clouddriveweb.utils

import io.github.mellivorines.clouddriveweb.utils.MD5Util.getMD5


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */

/**
 * 密码工具类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
object PasswordUtil {
    val salt: String
        /**
         * 随机生成盐值
         *
         * @return
         */
        get() = getMD5(UUIDUtil.getUUID())

    /**
     * 密码加密
     *
     * @param salt
     * @param inputPassword
     * @return
     */
    fun encryptPassword(salt: String, inputPassword: String?): String {
        return getMD5(getMD5(inputPassword!!) + salt)
    }
}
