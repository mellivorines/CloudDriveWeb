package io.github.mellivorines.clouddriveweb.dao.entity

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import org.babyfish.jimmer.sql.*


/**
 * <p>
 *  物理文件信息表
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Entity
@Table(name = "cloud_drive_file")
@Schema(description = " 物理文件信息表", title = " 物理文件信息表")
interface File {

    /**
     *  文件id */
    @Id
    @Column(name = "file_id")
    @get:Schema(description = " 文件id ")
    val fileId: Long

    /**
     *  文件名称 */
    @get:Schema(description = " 文件名称 ")
    val filename: String

    /**
     *  文件物理路径 */
    @Column(name = "real_path")
    @get:Schema(description = " 文件物理路径 ")
    val realPath: String

    /**
     *  文件实际大小 */
    @Column(name = "file_size")
    @get:Schema(description = " 文件实际大小 ")
    val fileSize: String

    /**
     *  文件大小展示字符 */
    @Column(name = "file_size_desc")
    @get:Schema(description = " 文件大小展示字符 ")
    val fileSizeDesc: String

    /**
     *  文件后缀 */
    @Column(name = "file_suffix")
    @get:Schema(description = " 文件后缀 ")
    val fileSuffix: String

    /**
     *  文件预览的响应头Content-Type的值 */
    @Column(name = "file_preview_content_type")
    @get:Schema(description = " 文件预览的响应头Content-Type的值 ")
    val filePreviewContentType: String

    /**
     *  文件唯一标识 */
    @get:Schema(description = " 文件唯一标识 ")
    val md5: String

    /**
     *  创建人 */
    @Column(name = "create_user")
    @get:Schema(description = " 创建人 ")
    val createUser: Long

    /**
     *  创建时间 */
    @Column(name = "create_time")
    @get:Schema(description = " 创建时间 ")
    val createTime: LocalDateTime
}
