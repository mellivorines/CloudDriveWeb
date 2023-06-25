package io.github.mellivorines.clouddriveweb.model

import io.github.mellivorines.clouddriveweb.enum.ResponseCodeEnum
import java.io.Serializable


/**
 * @Description:响应模型
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/23
 */
data class ResultModel(
        var code: Int,
        var msg: String?,
        var data: Any?
) : Serializable

fun success(data: Any?): ResultModel {
    return success(ResponseCodeEnum.CODE_200.code, ResponseCodeEnum.CODE_200.msg, data)
}

fun fail(msg: String?): ResultModel {
    return fail(ResponseCodeEnum.CODE_500.code, msg, msg)
}

private fun success(code: Int, msg: String?, data: Any?): ResultModel {
    return ResultModel(code, msg, data)
}

private fun fail(code: Int, msg: String?, data: Any?): ResultModel {
    return ResultModel(code, msg, data)
}