package io.github.mellivorines.cloud.drive.web.exception


/**
 * @Description:自定义异常
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/2
 */
data class BizException(
    /**
     * 错误码
     */
    private var code: Int? = null,

    /**
     * 错误信息
     */
    private var msg: String? = null
) : RuntimeException() {

    override fun toString(): String {
        return "BizException:{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}'
    }
}