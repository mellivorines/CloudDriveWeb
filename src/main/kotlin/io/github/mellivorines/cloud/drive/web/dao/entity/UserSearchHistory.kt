package io.github.mellivorines.cloud.drive.web.dao.entity

import io.github.mellivorines.cloud.drive.web.utils.MyUUIDIdGenerator
import io.swagger.v3.oas.annotations.media.Schema
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
interface UserSearchHistory:BaseEntity {

    /**
     *  主键 */
    @Id
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    @get:Schema(description = " 主键 ")
    val id: String

    /**
     *  用户id */
    @Key
    @Column(name = "user_id")
    @get:Schema(description = " 用户id ")
    val userId: String

    /**
     *  搜索文案 */
    @Key
    @Column(name = "search_content")
    @get:Schema(description = " 搜索文案 ")
    val searchContent: String
}
