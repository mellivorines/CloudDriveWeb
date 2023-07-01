package io.github.mellivorines.clouddriveweb.dao.input

import java.time.LocalDateTime
import io.github.mellivorines.clouddriveweb.dao.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import org.mapstruct.BeanMapping
import org.babyfish.jimmer.Input
import org.mapstruct.Mapper

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
    val userId: Long,

    /**
     *  用户名 */
    @Schema(description = " 用户名 ")
    val username: String,

    /**
     *  密码 */
    @Schema(description = " 密码 ")
    val password: String,

    /**
     *  随机盐值 */
    @Schema(description = " 随机盐值 ")
    val salt: String,

    /**
     *  密保问题 */
    @Schema(description = " 密保问题 ")
    val question: String,

    /**
     *  密保答案 */
    @Schema(description = " 密保答案 ")
    val answer: String,

    /**
     *  创建时间 */
    @Schema(description = " 创建时间 ")
    val createTime: LocalDateTime,

    /**
     *  更新时间 */
    @Schema(description = " 更新时间 ")
    val updateTime: LocalDateTime,

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

