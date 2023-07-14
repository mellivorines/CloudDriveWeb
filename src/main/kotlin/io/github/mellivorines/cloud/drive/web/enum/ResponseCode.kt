package io.github.mellivorines.cloud.drive.web.enum


/**
 * @Description:公用返回码枚举类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/2
 */

enum class ResponseCode(val code: Int, val msg: String) {
    CODE_100(100, "程序内部户错误"),
    CODE_200(200, "成功"),
    CODE_404(404, "地址不存在"),
    CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_504(504, "需要登录"),
    CODE_505(505, "token过期"),
    CODE_506(506, "需要分享码"),
    CODE_507(507, "分享的文件丢失"),
    CODE_508(508, "分享已取消"),
    CODE_509(509, "分享已过期"),
    CODE_600(600, "参数错误"),
    CODE_601(601, "信息已经存在"),
    CODE_901(901, "登录超时，请重新登录"),
    CODE_902(902, "分享连接不存在，或者已失效"),
    CODE_903(903, "分享验证失效，请重新验证"),
    CODE_904(904, "网盘空间不足，请扩容");
}

