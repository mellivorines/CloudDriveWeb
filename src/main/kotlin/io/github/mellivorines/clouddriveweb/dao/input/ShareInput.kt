package io.github.mellivorines.clouddriveweb.dao.input

import java.time.LocalDateTime
import io.github.mellivorines.clouddriveweb.dao.entity.Share
import io.swagger.v3.oas.annotations.media.Schema
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import org.mapstruct.BeanMapping
import org.babyfish.jimmer.Input
import org.mapstruct.Mapper

/**
 * <p>
 * ShareInput Share实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Schema(description = " 用户分享表", title = " 用户分享表")
data class ShareInput(
    /**
     *  分享id */
    @Schema(description = " 分享id ")
    val shareId: Long,

    /**
     *  分享名称 */
    @Schema(description = " 分享名称 ")
    val shareName: String,

    /**
     *  分享类型（0 有提取码） */
    @Schema(description = " 分享类型（0 有提取码） ")
    val shareType: Int,

    /**
     *  分享类型（0 永久有效；1 7天有效；2 30天有效） */
    @Schema(description = " 分享类型（0 永久有效；1 7天有效；2 30天有效） ")
    val shareDayType: Int,

    /**
     *  分享有效天数（永久有效为0） */
    @Schema(description = " 分享有效天数（永久有效为0） ")
    val shareDay: Int,

    /**
     *  分享结束时间 */
    @Schema(description = " 分享结束时间 ")
    val shareEndTime: LocalDateTime,

    /**
     *  分享链接地址 */
    @Schema(description = " 分享链接地址 ")
    val shareUrl: String,

    /**
     *  分享提取码 */
    @Schema(description = " 分享提取码 ")
    val shareCode: String,

    /**
     *  分享状态（0 正常；1 有文件被删除） */
    @Schema(description = " 分享状态（0 正常；1 有文件被删除） ")
    val shareStatus: Int,

    /**
     *  分享创建人 */
    @Schema(description = " 分享创建人 ")
    val createUser: Long,

    /**
     *  创建时间 */
    @Schema(description = " 创建时间 ")
    val createTime: LocalDateTime,

    ) : Input<Share> {

    override fun toEntity(): Share = CONVERTER.toShare(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toShare(input: ShareInput): Share
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

