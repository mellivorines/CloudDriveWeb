package io.github.mellivorines.cloud.drive.web.dao.input

import io.github.mellivorines.cloud.drive.web.dao.entity.UserSearchHistory
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.Input
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

/**
 * <p>
 * UserSearchHistoryInput UserSearchHistory实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Schema(description = " 用户搜索历史表", title = " 用户搜索历史表")
data class UserSearchHistoryInput(
    /**
     *  主键 */
    @Schema(description = " 主键 ")
    val id: String,

    /**
     *  用户id */
    @Schema(description = " 用户id ")
    val userId: String,

    /**
     *  搜索文案 */
    @Schema(description = " 搜索文案 ")
    val searchContent: String,

    /**
     *  创建时间 */
    @Schema(description = " 创建时间 ")
    val createTime: LocalDateTime,

    /**
     *  更新时间 */
    @Schema(description = " 更新时间 ")
    val updateTime: LocalDateTime,

    ) : Input<UserSearchHistory> {

    override fun toEntity(): UserSearchHistory = CONVERTER.toUserSearchHistory(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toUserSearchHistory(input: UserSearchHistoryInput): UserSearchHistory
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

