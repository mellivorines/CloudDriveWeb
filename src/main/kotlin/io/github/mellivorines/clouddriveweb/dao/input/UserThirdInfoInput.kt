package io.github.mellivorines.clouddriveweb.dao.input

import io.github.mellivorines.clouddriveweb.dao.entity.UserThirdInfo
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.Input
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers

/**
 * <p>
 * UserThirdInfoInput UserThirdInfo实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Schema(description = "用户第三方信息", title = "用户第三方信息")
data class UserThirdInfoInput(
    /**
     *  用户第三方信息ID */
    @Schema(description = "用户第三方信息ID")
    val userThirdInfoId: String?,

    /**
     *  第三方OpenID */
    @Schema(description = "第三方OpenID")
    val thirdOpenId: String?,

    /**
     *  第三方头像 */
    @Schema(description = "第三方头像")
    val thirdAvatar: String?,

    /**
     *  第三方信息类型：1QQ;2微信; */
    val type: Int?,

    ) : Input<UserThirdInfo> {

    override fun toEntity(): UserThirdInfo = CONVERTER.toUserThirdInfo(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toUserThirdInfo(input: UserThirdInfoInput): UserThirdInfo
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

