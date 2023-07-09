package io.github.mellivorines.cloud.drive.web.model.vo

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/9
 */
@Schema(title = "文件上传VO")
data class FileUploadVO(
    @Schema(description = "文件名称")
    val name: String,

    @Schema(description = "用户ID")
    val userId: String,

    @Schema(description = "文件对应的md5")
    val md5: String,

    @Schema(description = "总分片数")
    val chunks: Int? = 0,

    @Schema(description = "分片下标")
    val chunk: Int? = null,

    @Schema(description = "文件大小", required = true)
    val size: Long,

    @Schema(description = "父id", required = true)
    val parentId: String,

    @Schema(description = "上传文件", required = true)
    val file: MultipartFile
){
    /**
     * 校验是不是分片上传
     *
     * @return
     */
    fun isChunked(): Boolean {
        return !Objects.isNull(chunk) && !Objects.isNull(chunks)
    }
}
