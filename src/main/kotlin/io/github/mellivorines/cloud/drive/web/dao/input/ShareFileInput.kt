package io.github.mellivorines.cloud.drive.web.dao.input

import io.github.mellivorines.cloud.drive.web.dao.entity.ShareFile
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.Input
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

/**
 * <p>
 * ShareFileInput ShareFile实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Schema(description = " 用户分享文件表", title = " 用户分享文件表")
data class ShareFileInput(
    /**
     *  主键 */
    @Schema(description = " 主键 ")
    val id: String,

    /**
     *  分享id */
    @Schema(description = " 分享id ")
    val shareId: String,

    /**
     *  文件记录ID */
    @Schema(description = " 文件记录ID ")
    val fileId: String,

    /**
     *  分享创建人 */
    @Schema(description = " 分享创建人 ")
    val createUser: String,

    /**
     *  创建时间 */
    @Schema(description = " 创建时间 ")
    val createTime: LocalDateTime,

    ) : Input<ShareFile> {

    override fun toEntity(): ShareFile = CONVERTER.toShareFile(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toShareFile(input: ShareFileInput): ShareFile
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

