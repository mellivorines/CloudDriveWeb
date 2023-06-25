package io.github.mellivorines.clouddriveweb.dao.entity

import io.github.mellivorines.clouddriveweb.utils.MyUUIDIdGenerator
import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator


/**
 * <p>
 *  文件信息
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Entity
@Table(name = "file_info")
interface FileInfo {

    /**
     *  文件ID */
    @Id
    @Column(name = "file_info_id")
    @GeneratedValue(generatorType = MyUUIDIdGenerator::class)
    val fileInfoId: String

    /**
     *  用户ID */
    @Key
    @Column(name = "user_id")
    val userId: String

    /**
     *  md5值，第一次上传记录 */
    @Column(name = "file_md5")
    val fileMd5: String?

    /**
     *  父级ID */
    @Column(name = "file_pid")
    val filePid: String?

    /**
     *  文件大小 */
    @Column(name = "file_size")
    val fileSize: Long?

    /**
     *  文件名称 */
    @Column(name = "file_name")
    val fileName: String?

    /**
     *  封面 */
    @Column(name = "file_cover")
    val fileCover: String?

    /**
     *  文件路径 */
    @Column(name = "file_path")
    val filePath: String?

    /**
     *  创建时间 */
    @Column(name = "create_time")
    val createTime: LocalDateTime?

    /**
     *  最后更新时间 */
    @Column(name = "last_update_time")
    val lastUpdateTime: LocalDateTime?

    /**
     *  0:文件 1:目录 */
    @Column(name = "folder_type")
    val folderType: Int?

    /**
     *  1:视频 2:音频  3:图片 4:文档 5:其他 */
    @Column(name = "file_category")
    val fileCategory: Int?

    /**
     *   1:视频 2:音频  3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他 */
    @Column(name = "file_type")
    val fileType: Int?

    /**
     *  0:转码中 1转码失败 2:转码成功 */
    val status: Int?

    /**
     *  回收站时间 */
    @Column(name = "recovery_time")
    val recoveryTime: LocalDateTime?

    /**
     *  删除标记 0:删除  1:回收站  2:正常 */
    @Column(name = "del_flag")
    val delFlag: Int?
}
