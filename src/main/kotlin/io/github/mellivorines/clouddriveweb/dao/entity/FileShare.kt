package io.github.mellivorines.clouddriveweb.dao.entity

import io.github.mellivorines.clouddriveweb.utils.MyUUIDIdGenerator
import org.babyfish.jimmer.sql.*
import java.time.LocalDateTime


/**
 * <p>
 *  分享信息
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Entity
@Table(name = "file_share")
interface FileShare {

    /**
     *  分享ID */
    @Id
    @Column(name = "file_share_id")
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    val fileShareId: String

    /**
     *  文件ID */
    @Column(name = "file_info_id")
    val fileInfoId: String

    /**
     *  用户ID */
    @Column(name = "user_id")
    val userId: String

    /**
     *  有效期类型 0:1天 1:7天 2:30天 3:永久有效 */
    @Column(name = "valid_type")
    val validType: Int?

    /**
     *  失效时间 */
    @Column(name = "expire_time")
    val expireTime: LocalDateTime?

    /**
     *  分享时间 */
    @Column(name = "share_time")
    val shareTime: LocalDateTime?

    /**
     *  提取码 */
    val code: String?

    /**
     *  浏览次数 */
    @Column(name = "show_count")
    val showCount: Int?
}
