package io.github.mellivorines.cloud.drive.web.dao.entity

import io.github.mellivorines.cloud.drive.web.utils.MyUUIDIdGenerator
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.*
import java.time.LocalDateTime


/**
 * <p>
 *  用户分享文件表
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Entity
@Table(name = "cloud_drive_share_file")
@Schema(description = " 用户分享文件表", title = " 用户分享文件表")
interface ShareFile {

    /**
     *  主键 */
    @Id
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    @get:Schema(description = " 主键 ")
    val id: String

    /**
     *  分享id */
    @Key
    @Column(name = "share_id")
    @get:Schema(description = " 分享id ")
    val shareId: String

    /**
     *  文件记录ID */
    @Key
    @Column(name = "file_id")
    @get:Schema(description = " 文件记录ID ")
    val fileId: String

    /**
     *  分享创建人 */
    @Column(name = "create_user")
    @get:Schema(description = " 分享创建人 ")
    val createUser: String

    /**
     *  创建时间 */
    @Column(name = "create_time")
    @get:Schema(description = " 创建时间 ")
    val createTime: LocalDateTime
}
