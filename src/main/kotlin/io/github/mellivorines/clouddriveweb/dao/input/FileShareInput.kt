package io.github.mellivorines.clouddriveweb.dao.input

import io.github.mellivorines.clouddriveweb.dao.entity.FileShare
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.Input
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

/**
 * <p>
 * FileShareInput FileShare实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Schema(description = "文件分享", title = "文件分享")
data class FileShareInput(
    /**
     *  分享ID */
    @Schema(description = "分享ID")
    val shareId: String?,

    /**
     *  文件ID */
    @Schema(description = "文件ID")
    val fileId: String,

    /**
     *  用户ID */
    @Schema(description = "用户ID")
    val userId: String,

    /**
     *  有效期类型 0:1天 1:7天 2:30天 3:永久有效 */
    @Schema(description = "有效期类型 0:1天 1:7天 2:30天 3:永久有效")
    val validType: Int?,

    /**
     *  失效时间 */
    @Schema(description = "失效时间")
    val expireTime: LocalDateTime?,

    /**
     *  分享时间 */
    @Schema(description = "分享时间")
    val shareTime: LocalDateTime?,

    /**
     *  提取码 */
    @Schema(description = "提取码")
    val code: String?,

    /**
     *  浏览次数 */
    @Schema(description = "浏览次数")
    val showCount: Int?,

    ) : Input<FileShare> {

    override fun toEntity(): FileShare = CONVERTER.toFileShare(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toFileShare(input: FileShareInput): FileShare
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

