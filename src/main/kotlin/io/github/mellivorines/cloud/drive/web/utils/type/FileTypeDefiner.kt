package io.github.mellivorines.cloud.drive.web.utils.type

import io.github.mellivorines.cloud.drive.web.utils.FileUtil.getFileSuffix


/**
 * @Description:文件类型定义接口
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
interface FileTypeDefiner {
    /**
     * 判断是否匹配该类型
     *
     * @param filename
     * @return
     */
     fun isMatched(filename: String): Boolean {
        val fileSuffix = getFileSuffix(filename)
        return if (EMPTY_STR == fileSuffix) {
            false
        } else includeSuffixes.contains(fileSuffix)
    }

    /**
     * 获取该文件类型所包含的文件的后缀（后缀统一大写）
     *
     * @return
     */
    val includeSuffixes: List<String>

    /**
     * 获取文件类型标识
     *
     * @return
     */
    val fileTypeCode: Int

    /**
     * 获取该文件类型的描述
     *
     * @return
     */
    val fileTypeDesc: String

    /**
     * 获取该定义器的顺序 从大到小倒序
     *
     * @return
     */
    val order: Int

    companion object {
        const val EMPTY_STR = ""
    }
}

