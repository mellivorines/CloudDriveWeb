package io.github.mellivorines.cloud.drive.web.model

import io.github.mellivorines.cloud.drive.web.enum.ResponseCode
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable


/**
 * @Description:响应模型
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/23
 */
@Schema(description = "响应模型", title = "响应模型")
data class ResultModel(
    @Schema(description = "响应码")
    var code: Int,
    @Schema(description = "响应消息")
    var msg: String?,
    @Schema(description = "响应数据")
    var data: Any?
) : Serializable

fun success(data: Any?): ResultModel {
    return success(ResponseCode.CODE_200.code, ResponseCode.CODE_200.msg, data)
}

fun fail(msg: String?): ResultModel {
    return fail(ResponseCode.CODE_500.code, ResponseCode.CODE_500.msg, msg)
}

private fun success(code: Int, msg: String?, data: Any?): ResultModel {
    return ResultModel(code, msg, data)
}

private fun fail(code: Int, msg: String?, data: Any?): ResultModel {
    return ResultModel(code, msg, data)
}