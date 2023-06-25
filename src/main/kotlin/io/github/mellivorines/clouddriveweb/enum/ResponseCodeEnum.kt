package io.github.mellivorines.clouddriveweb.enum


/**
 * @Description:异常枚举信息
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/9
 */
enum class ResponseCodeEnum(val code: Int, val msg: String) {
    CODE_100(100, "程序内部户错误"),
    CODE_200(200, "成功"),
    CODE_404(404, "地址不存在"),
    CODE_600(600, "参数错误"),
    CODE_601(601, "信息已经存在"),
    CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_901(901, "登录超时，请重新登录"),
    CODE_902(902, "分享连接不存在，或者已失效"),
    CODE_903(903, "分享验证失效，请重新验证"),
    CODE_904(904, "网盘空间不足，请扩容")
}
