package io.github.mellivorines.cloud.drive.web.dao.input

import io.github.mellivorines.cloud.drive.web.dao.entity.UserFile
import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.Input
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

/**
 * <p>
 * UserFileInput UserFile实体的Input
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Schema(description = " 用户文件信息表", title = " 用户文件信息表")
data class UserFileInput(
    /**
     *  文件记录ID */
    @Schema(description = " 文件记录ID ")
    val fileId: String?,

    /**
     *  用户ID */
    @Schema(description = " 用户ID ")
    val userId: String,

    /**
     *  上级文件夹ID,顶级文件夹为0 */
    @Schema(description = " 上级文件夹ID,顶级文件夹为0 ")
    val parentId: String,

    /**
     *  真实文件id */
    @Schema(description = " 真实文件id ")
    val realFileId: String?,

    /**
     *  文件名 */
    @Schema(description = " 文件名 ")
    var filename: String,

    /**
     *  是否是文件夹 （0 否 1 是） */
    @Schema(description = " 是否是文件夹 （0 否 1 是） ")
    val folderFlag: Int,

    /**
     *  文件大小展示字符 */
    @Schema(description = " 文件大小展示字符 ")
    val fileSizeDesc: String?,

    /**
     *  文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv） */
    @Schema(description = " 文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv） ")
    val fileType: Int?,

    /**
     *  删除标识（0 否 1 是） */
    @Schema(description = " 删除标识（0 否 1 是） ")
    val delFlag: Int,

    /**
     *  创建人 */
    @Schema(description = " 创建人 ")
    val createUser: String?,

    /**
     *  创建时间 */
    @Schema(description = " 创建时间 ")
    val createTime: LocalDateTime?,

    /**
     *  更新人 */
    @Schema(description = " 更新人 ")
    val updateUser: String?,

    /**
     *  更新时间 */
    @Schema(description = " 更新时间 ")
    val updateTime: LocalDateTime?

    ) : Input<UserFile> {

    override fun toEntity(): UserFile = CONVERTER.toUserFile(this)

    @Mapper
    internal interface Converter {
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        fun toUserFile(input: UserFileInput): UserFile
    }

    companion object {
        @JvmStatic
        private val CONVERTER = Mappers.getMapper(Converter::class.java)
    }
}

