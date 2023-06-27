package io.github.mellivorines.clouddriveweb.dao.entity

import io.github.mellivorines.clouddriveweb.utils.MyUUIDIdGenerator
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.*
import java.time.LocalDateTime


/**
 * <p>
 *  用户信息
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Entity
@Table(name = "user")
@Schema(description = "用户信息", title = "用户信息")
interface User {

    /**
     *  用户ID */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    @get:Schema(description = "用户ID")
    val userId: String

    /**
     *  昵称 */
    @Key
    @Column(name = "nick_name")
    @get:Schema(description = "昵称")
    val nickName: String?

    /**
     *  用户名：可以使用邮箱或者电话号码 */
    @Key
    @Column(name = "user_name")
    @get:Schema(description = "用户名：可以使用邮箱或者电话号码")
    val userName: String

    /**
     *  密码 */
    @get:Schema(description = "密码")
    val password: String

    /**
     *  用户第三方信息ID */
    @Column(name = "user_third_info_id")
    @get:Schema(description = "用户第三方信息ID")
    val userThirdInfoId: String?

    /**
     *  加入时间 */
    @Column(name = "join_time")
    @get:Schema(description = "加入时间")
    val joinTime: LocalDateTime?

    /**
     *  最后登录时间 */
    @Column(name = "last_login_time")
    @get:Schema(description = "最后登录时间")
    val lastLoginTime: LocalDateTime?

    /**
     *  0:禁用 1:正常 */
    @get:Schema(description = "0:禁用 1:正常")
    val status: Int?

    /**
     *  使用空间单位byte */
    @Column(name = "use_space")
    @get:Schema(description = "使用空间单位byte")
    val useSpace: Long?

    /**
     *  总空间 */
    @Column(name = "total_space")
    @get:Schema(description = "总空间")
    val totalSpace: Long?
}
