package io.github.mellivorines.clouddriveweb.dao.input

import io.github.mellivorines.clouddriveweb.dao.entity.UserThirdInfo
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import org.mapstruct.BeanMapping
import org.babyfish.jimmer.Input
import org.mapstruct.Mapper

/**
 * <p>
 * UserThirdInfoInput UserThirdInfo实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
data class UserThirdInfoInput(
        /**
         *  用户第三方信息ID */
        val userThirdInfoId: String?,

        /**
         *  第三方OpenID */
        val thirdOpenId: String?,

        /**
         *  第三方头像 */
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

