package io.github.mellivorines.cloud.drive.web.utils

import java.security.MessageDigest
import java.util.*


/**
 * @Description:MD5加密工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object MD5Util {
    private const val UTF_8_STR = "UTF-8"
    private const val MD5_STR = "MD5"
    private const val ZERO_STR = "0"

    /**
     * 获取md5加密串
     *
     * @param message
     * @return
     */
    fun getMD5(message: String): String {
        var md5 = ""
        try {
            // 创建一个md5算法对象
            val md = MessageDigest.getInstance(MD5_STR)
            val messageByte = message.toByteArray(charset(UTF_8_STR))
            // 获得MD5字节数组,16 * 8 = 128位
            val md5Byte = md.digest(messageByte)
            // 转换为16进制字符串
            md5 = bytesToHex(md5Byte)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return md5
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexStr = StringBuffer()
        var num: Int
        for (i in bytes.indices) {
            num = bytes[i].toInt()
            if (num < 0) {
                num += 256
            }
            if (num < 16) {
                hexStr.append(ZERO_STR)
            }
            hexStr.append(Integer.toHexString(num))
        }
        return hexStr.toString().uppercase(Locale.getDefault())
    }
}
