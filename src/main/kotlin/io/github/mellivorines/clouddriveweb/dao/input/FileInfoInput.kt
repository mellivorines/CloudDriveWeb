package io.github.mellivorines.clouddriveweb.dao.input

import java.time.LocalDateTime
import io.github.mellivorines.clouddriveweb.dao.entity.FileInfo
import io.swagger.v3.oas.annotations.media.Schema
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import org.mapstruct.BeanMapping
import org.babyfish.jimmer.Input
import org.mapstruct.Mapper

/**
 * <p>
 * FileInfoInput FileInfo实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Schema(description = "文件信息", title = "文件信息")
data class FileInfoInput(
        /**
         *  文件ID */
        @Schema(description = "文件ID")
        val fileId: String?,

        /**
         *  用户ID */
        @Schema(description = "用户ID")
        val userId: String,

        /**
         *  md5值，第一次上传记录 */
        @Schema(description = "md5值，第一次上传记录")
        val fileMd5: String?,

        /**
         *  父级ID */
        @Schema(description = "父级ID")
        val filePid: String?,

        /**
         *  文件大小 */
        @Schema(description = "文件大小")
        val fileSize: Long?,

        /**
         *  文件名称 */
        @Schema(description = "文件名称")
        val fileName: String?,

        /**
         *  封面 */
        @Schema(description = "封面")
        val fileCover: String?,

        /**
         *  文件路径 */
        @Schema(description = "文件路径")
        val filePath: String?,

        /**
         *  创建时间 */
        @Schema(description = "创建时间")
        val createTime: LocalDateTime?,

        /**
         *  更新时间 */
        @Schema(description = "更新时间")
        val lastUpdateTime: LocalDateTime?,

        /**
         *  文件类型：0:文件 1:目录 */
        @Schema(description = "文件类型：0:文件 1:目录")
        val folderType: Int?,

        /**
         *  1:视频 2:音频  3:图片 4:文档 5:其他 */
        @Schema(description = "1:视频 2:音频  3:图片 4:文档 5:其他")
        val fileCategory: Int?,

        /**
         *   类型:1:视频 2:音频  3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他 */
        @Schema(description = "类型:1:视频 2:音频  3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他")
        val fileType: Int?,

        /**
         *  状态:0:转码中 1转码失败 2:转码成功 */
        @Schema(description = "状态:0:转码中 1转码失败 2:转码成功")
        val status: Int?,

        /**
         *  回收站时间 */
        @Schema(description = "回收站时间")
        val recoveryTime: LocalDateTime?,

        /**
         *  删除标记 0:删除  1:回收站  2:正常 */
        @Schema(description = "删除标记 0:删除  1:回收站  2:正常")
        val delFlag: Int?,

        ) : Input<FileInfo> {

    override fun toEntity(): FileInfo = CONVERTER.toFileInfo(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toFileInfo(input: FileInfoInput): FileInfo
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

