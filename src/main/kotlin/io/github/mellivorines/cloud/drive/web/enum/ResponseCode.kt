package io.github.mellivorines.cloud.drive.web.enum


/**
 * @Description:公用返回码枚举类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/2
 */

enum class ResponseCode(val code: Int, val msg: String) {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 错误
     */
    ERROR(1, "错误"),

    /**
     * 需要登录
     */
    NEED_LOGIN(10, "需要登录"),

    /**
     * token过期
     */
    TOKEN_EXPIRE(2, "token过期"),

    /**
     * 需要分享码
     */
    NEED_SHARE_CODE(4, "需要分享码"),

    /**
     * 分享的文件丢失
     */
    SHARE_FILE_MISS(5, "分享的文件丢失"),

    /**
     * 分享已取消
     */
    SHARE_CANCELLED(6, "分享已取消"),

    /**
     * 分享已过期
     */
    SHARE_EXPIRE(7, "分享已过期"),

    /**
     * 参数错误
     */
    ERROR_PARAM(3, "参数错误"),

    CODE_100(100, "程序内部户错误"),
    CODE_200(200, "成功"),
    CODE_404(404, "地址不存在"),
    CODE_600(600, "参数错误"),
    CODE_601(601, "信息已经存在"),
    CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_901(901, "登录超时，请重新登录"),
    CODE_902(902, "分享连接不存在，或者已失效"),
    CODE_903(903, "分享验证失效，请重新验证"),
    CODE_904(904, "网盘空间不足，请扩容");
}

