package io.github.mellivorines.clouddriveweb.dao.entity

import io.github.mellivorines.clouddriveweb.utils.MyUUIDIdGenerator
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator


/**
 * <p>
 *  用户第三方信息
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Entity
@Table(name = "user_third_info")
interface UserThirdInfo {

    /**
     *  用户第三方信息ID */
    @Id
    @Column(name = "user_third_info_id")
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    val userThirdInfoId: String

    /**
     *  第三方OpenID */
    @Column(name = "third_open_id")
    val thirdOpenId: String?

    /**
     *  第三方头像 */
    @Column(name = "third_avatar")
    val thirdAvatar: String?

    /**
     *  第三方信息类型：1QQ;2微信; */
    val type: Int?
}
