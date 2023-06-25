package io.github.mellivorines.clouddriveweb.dao.input

import io.github.mellivorines.clouddriveweb.dao.entity.User
import org.babyfish.jimmer.Input
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

/**
 * <p>
 * UserInput User实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
data class UserInput(
        /**
         *  用户ID */
        val userId: String?,

        /**
         *  昵称 */
        val nickName: String?,

        /**
         *  用户名：可以使用邮箱或者电话号码 */
        val userName: String,

        /**
         *  密码 */
        val password: String,

        /**
         *  用户第三方信息ID */
        val userThirdInfoId: String?,

        /**
         *  加入时间 */
        var joinTime: LocalDateTime?,

        /**
         *  最后登录时间 */
        var lastLoginTime: LocalDateTime?,

        /**
         *  0:禁用 1:正常 */
        var status: Int?,

        /**
         *  使用空间单位byte */
        val useSpace: Long?,

        /**
         *  总空间 */
        val totalSpace: Long?,

        ) : Input<User> {

    override fun toEntity(): User = CONVERTER.toUser(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toUser(input: UserInput): User
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

