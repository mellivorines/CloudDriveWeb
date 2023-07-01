package io.github.mellivorines.clouddriveweb.dao.input

import java.time.LocalDateTime
import io.github.mellivorines.clouddriveweb.dao.entity.File
import io.swagger.v3.oas.annotations.media.Schema
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import org.mapstruct.BeanMapping
import org.babyfish.jimmer.Input
import org.mapstruct.Mapper

/**
 * <p>
 * FileInput File实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Schema(description = " 物理文件信息表", title = " 物理文件信息表")
data class FileInput(
    /**
     *  文件id */
    @Schema(description = " 文件id ")
    val fileId: Long,

    /**
     *  文件名称 */
    @Schema(description = " 文件名称 ")
    val filename: String,

    /**
     *  文件物理路径 */
    @Schema(description = " 文件物理路径 ")
    val realPath: String,

    /**
     *  文件实际大小 */
    @Schema(description = " 文件实际大小 ")
    val fileSize: String,

    /**
     *  文件大小展示字符 */
    @Schema(description = " 文件大小展示字符 ")
    val fileSizeDesc: String,

    /**
     *  文件后缀 */
    @Schema(description = " 文件后缀 ")
    val fileSuffix: String,

    /**
     *  文件预览的响应头Content-Type的值 */
    @Schema(description = " 文件预览的响应头Content-Type的值 ")
    val filePreviewContentType: String,

    /**
     *  文件唯一标识 */
    @Schema(description = " 文件唯一标识 ")
    val md5: String,

    /**
     *  创建人 */
    @Schema(description = " 创建人 ")
    val createUser: Long,

    /**
     *  创建时间 */
    @Schema(description = " 创建时间 ")
    val createTime: LocalDateTime,

    ) : Input<File> {

    override fun toEntity(): File = CONVERTER.toFile(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toFile(input: FileInput): File
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

