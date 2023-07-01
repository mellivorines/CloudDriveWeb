package io.github.mellivorines.clouddriveweb.dao.entity

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*


/**
 * <p>
 *  用户信息表
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Entity
@Table(name = "cloud_drive_user")
@Schema(description = " 用户信息表", title = " 用户信息表")
interface User {

    /**
     *  用户id */
    @Id
    @Column(name = "user_id")
    @get:Schema(description = " 用户id ")
    val userId: Long

    /**
     *  用户名 */
    @Key
    @get:Schema(description = " 用户名 ")
    val username: String

    /**
     *  密码 */
    @get:Schema(description = " 密码 ")
    val password: String

    /**
     *  随机盐值 */
    @get:Schema(description = " 随机盐值 ")
    val salt: String

    /**
     *  密保问题 */
    @get:Schema(description = " 密保问题 ")
    val question: String

    /**
     *  密保答案 */
    @get:Schema(description = " 密保答案 ")
    val answer: String

    /**
     *  创建时间 */
    @Column(name = "create_time")
    @get:Schema(description = " 创建时间 ")
    val createTime: LocalDateTime

    /**
     *  更新时间 */
    @Column(name = "update_time")
    @get:Schema(description = " 更新时间 ")
    val updateTime: LocalDateTime
}
