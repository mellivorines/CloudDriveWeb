package io.github.mellivorines.clouddriveweb.dao.entity

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*


/**
 * <p>
 *  用户搜索历史表
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Entity
@Table(name = "cloud_drive_user_search_history")
@Schema(description = " 用户搜索历史表", title = " 用户搜索历史表")
interface UserSearchHistory {

    /**
     *  主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Schema(description = " 主键 ")
    val id: Long

    /**
     *  用户id */
    @Key
    @Column(name = "user_id")
    @get:Schema(description = " 用户id ")
    val userId: Long

    /**
     *  搜索文案 */
    @Key
    @Column(name = "search_content")
    @get:Schema(description = " 搜索文案 ")
    val searchContent: String

    /**
     *  创建时间 */
    @Column(name = "create_time")
    @get:Schema(description = " 创建时间 ")
    val createTime: LocalDateTime

    /**
     *  更新时间 */
    @Key
    @Column(name = "update_time")
    @get:Schema(description = " 更新时间 ")
    val updateTime: LocalDateTime
}
