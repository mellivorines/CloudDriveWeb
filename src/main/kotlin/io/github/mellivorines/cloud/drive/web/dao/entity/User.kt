package io.github.mellivorines.cloud.drive.web.dao.entity

import io.github.mellivorines.cloud.drive.web.utils.MyUUIDIdGenerator
import io.swagger.v3.oas.annotations.media.Schema
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
interface User:BaseEntity {

    /**
     *  用户id */
    @Id
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    @Column(name = "user_id")
    @get:Schema(description = " 用户id ")
    val userId: String

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
}
