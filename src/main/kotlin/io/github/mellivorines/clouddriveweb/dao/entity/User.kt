package io.github.mellivorines.clouddriveweb.dao.entity

import io.github.mellivorines.clouddriveweb.utils.MyUUIDIdGenerator
import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.util.UUID


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
interface User {

    /**
     *  用户ID */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    val userId: String

    /**
     *  昵称 */
    @Key
    @Column(name = "nick_name")
    val nickName: String?

    /**
     *  用户名：可以使用邮箱或者电话号码 */
    @Key
    @Column(name = "user_name")
    val userName: String

    /**
     *  密码 */
    val password: String

    /**
     *  用户第三方信息ID */
    @Column(name = "user_third_info_id")
    val userThirdInfoId: String?

    /**
     *  加入时间 */
    @Column(name = "join_time")
    val joinTime: LocalDateTime?

    /**
     *  最后登录时间 */
    @Column(name = "last_login_time")
    val lastLoginTime: LocalDateTime?

    /**
     *  0:禁用 1:正常 */
    val status: Int?

    /**
     *  使用空间单位byte */
    @Column(name = "use_space")
    val useSpace: Long?

    /**
     *  总空间 */
    @Column(name = "total_space")
    val totalSpace: Long?
}
