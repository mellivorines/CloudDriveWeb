package io.github.mellivorines.cloud.drive.web.utils.type.definer

import io.github.mellivorines.cloud.drive.web.utils.type.FileTypeDefiner


/**
 * @Description:视频文件定义类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
class TxtFileDefiner : FileTypeDefiner {

    /**
     * 文件类型标识
     */
    private val FILE_TYPE_CODE = 6

    /**
     * 文件类型描述
     */
    private val FILE_TYPE_DESC = "TXT"

    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一小写）
     *
     * @return
     */
    override val includeSuffixes: List<String>
        get() = mutableListOf(".txt")

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