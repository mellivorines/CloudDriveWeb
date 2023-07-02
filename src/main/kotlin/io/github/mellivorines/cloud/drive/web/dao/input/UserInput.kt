package io.github.mellivorines.cloud.drive.web.dao.input

import io.github.mellivorines.cloud.drive.web.dao.entity.User
import io.swagger.v3.oas.annotations.media.Schema
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
 * @date 2023-07-01
 */
@Schema(description = " 用户信息表", title = " 用户信息表")
data class UserInput(
    /**
     *  用户id */
    @Schema(description = " 用户id ")
    var userId: String?,

    /**
     *  用户名 */
    @Schema(description = " 用户名 ")
    var username: String,

    /**
     *  密码 */
    @Schema(description = " 密码 ")
    var password: String,

    /**
     *  随机盐值 */
    @Schema(description = " 随机盐值 ")
    var salt: String?,

    /**
     *  密保问题 */
    @Schema(description = " 密保问题 ")
    var question: String?,

    /**
     *  密保答案 */
    @Schema(description = " 密保答案 ")
    var answer: String?,

    /**
     *  创建时间 */
    @Schema(description = " 创建时间 ")
    var createTime: LocalDateTime?,

    /**
     *  更新时间 */
    @Schema(description = " 更新时间 ")
    var updateTime: LocalDateTime?,

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

