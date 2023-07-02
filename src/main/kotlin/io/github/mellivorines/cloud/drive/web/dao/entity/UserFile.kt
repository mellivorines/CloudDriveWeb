package io.github.mellivorines.cloud.drive.web.dao.entity

import io.github.mellivorines.cloud.drive.web.utils.MyUUIDIdGenerator
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.*


/**
 * <p>
 *  用户文件信息表
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Entity
@Table(name = "cloud_drive_user_file")
@Schema(description = " 用户文件信息表", title = " 用户文件信息表")
interface UserFile:BaseEntity {

    /**
     *  文件记录ID */
    @Id
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    @Column(name = "file_id")
    @get:Schema(description = " 文件记录ID ")
    val fileId: String

    /**
     *  用户ID */
    @Column(name = "user_id")
    @get:Schema(description = " 用户ID ")
    val userId: String

    /**
     *  上级文件夹ID,顶级文件夹为0 */
    @Column(name = "parent_id")
    @get:Schema(description = " 上级文件夹ID,顶级文件夹为0 ")
    val parentId: String

    /**
     *  真实文件id */
    @Column(name = "real_file_id")
    @get:Schema(description = " 真实文件id ")
    val realFileId: String

    /**
     *  文件名 */
    @get:Schema(description = " 文件名 ")
    val filename: String

    /**
     *  是否是文件夹 （0 否 1 是） */
    @Column(name = "folder_flag")
    @get:Schema(description = " 是否是文件夹 （0 否 1 是） ")
    val folderFlag: Int

    /**
     *  文件大小展示字符 */
    @Column(name = "file_size_desc")
    @get:Schema(description = " 文件大小展示字符 ")
    val fileSizeDesc: String

    /**
     *  文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv） */
    @Column(name = "file_type")
    @get:Schema(description = " 文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv） ")
    val fileType: Int

    /**
     *  删除标识（0 否 1 是） */
    @Column(name = "del_flag")
    @get:Schema(description = " 删除标识（0 否 1 是） ")
    val delFlag: Int

    /**
     *  创建人 */
    @Column(name = "create_user")
    @get:Schema(description = " 创建人 ")
    val createUser: String

    /**
     *  更新人 */
    @Column(name = "update_user")
    @get:Schema(description = " 更新人 ")
    val updateUser: String
}
