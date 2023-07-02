package io.github.mellivorines.cloud.drive.web.utils.type.definer

import io.github.mellivorines.cloud.drive.web.utils.type.FileTypeDefiner


/**
 * @Description:压缩文件定义类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
class ArchiveFileDefiner : FileTypeDefiner {

    /**
     * 文件类型标识
     */
    private val FILE_TYPE_CODE = 2

    /**
     * 文件类型描述
     */
    private val FILE_TYPE_DESC = "ARCHIVE"


    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一大写）
     *
     * @return
     */
    override val includeSuffixes: List<String>
        get() = mutableListOf(
            ".rar",
            ".zip",
            ".cab",
            ".iso",
            ".jar",
            ".ace",
            ".7z",
            ".tar",
            ".gz",
            ".arj",
            ".lah",
            ".uue",
            ".bz2",
            ".z",
            ".war"
        )

    /**
     * 获取文件类型标识
     *
     * @return
     */
    override val fileTypeCode: Int
        get() = this.FILE_TYPE_CODE

    /**
     * 获取该文件类型的描述
     *
     * @return
     */
    override val fileTypeDesc: String
        get() = this.FILE_TYPE_DESC

    /**
     * 获取该定义器的顺序 从大到小倒序
     *
     * @return
     */
    override val order: Int
        get() = this.FILE_TYPE_CODE
}

