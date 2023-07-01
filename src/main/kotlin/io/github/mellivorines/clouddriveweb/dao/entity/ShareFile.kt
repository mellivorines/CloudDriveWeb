package io.github.mellivorines.clouddriveweb.dao.entity

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Schema(description = " 主键 ")
    val id: Long

    /**
     *  分享id */
    @Key
    @Column(name = "share_id")
    @get:Schema(description = " 分享id ")
    val shareId: Long

    /**
     *  文件记录ID */
    @Key
    @Column(name = "file_id")
    @get:Schema(description = " 文件记录ID ")
    val fileId: Long

    /**
     *  分享创建人 */
    @Column(name = "create_user")
    @get:Schema(description = " 分享创建人 ")
    val createUser: Long

    /**
     *  创建时间 */
    @Column(name = "create_time")
    @get:Schema(description = " 创建时间 ")
    val createTime: LocalDateTime
}
