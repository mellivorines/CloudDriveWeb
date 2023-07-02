package io.github.mellivorines.cloud.drive.web.dao.entity

import io.github.mellivorines.cloud.drive.web.utils.MyUUIDIdGenerator
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.*
import java.time.LocalDateTime


/**
 * <p>
 *  用户分享表
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Entity
@Table(name = "cloud_drive_share")
@Schema(description = " 用户分享表", title = " 用户分享表")
interface Share {

    /**
     *  分享id */
    @Id
    @Column(name = "share_id")
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    @get:Schema(description = " 分享id ")
    val shareId: String

    /**
     *  分享名称 */
    @Column(name = "share_name")
    @get:Schema(description = " 分享名称 ")
    val shareName: String

    /**
     *  分享类型（0 有提取码） */
    @Column(name = "share_type")
    @get:Schema(description = " 分享类型（0 有提取码） ")
    val shareType: Int

    /**
     *  分享类型（0 永久有效；1 7天有效；2 30天有效） */
    @Column(name = "share_day_type")
    @get:Schema(description = " 分享类型（0 永久有效；1 7天有效；2 30天有效） ")
    val shareDayType: Int

    /**
     *  分享有效天数（永久有效为0） */
    @Column(name = "share_day")
    @get:Schema(description = " 分享有效天数（永久有效为0） ")
    val shareDay: Int

    /**
     *  分享结束时间 */
    @Column(name = "share_end_time")
    @get:Schema(description = " 分享结束时间 ")
    val shareEndTime: LocalDateTime

    /**
     *  分享链接地址 */
    @Column(name = "share_url")
    @get:Schema(description = " 分享链接地址 ")
    val shareUrl: String

    /**
     *  分享提取码 */
    @Column(name = "share_code")
    @get:Schema(description = " 分享提取码 ")
    val shareCode: String

    /**
     *  分享状态（0 正常；1 有文件被删除） */
    @Column(name = "share_status")
    @get:Schema(description = " 分享状态（0 正常；1 有文件被删除） ")
    val shareStatus: Int

    /**
     *  分享创建人 */
    @Key
    @Column(name = "create_user")
    @get:Schema(description = " 分享创建人 ")
    val createUser: String

    /**
     *  创建时间 */
    @Key
    @Column(name = "create_time")
    @get:Schema(description = " 创建时间 ")
    val createTime: LocalDateTime
}
