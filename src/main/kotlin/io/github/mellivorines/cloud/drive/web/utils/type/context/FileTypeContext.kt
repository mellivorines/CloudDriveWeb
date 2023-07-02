package io.github.mellivorines.cloud.drive.web.utils.type.context

import io.github.mellivorines.cloud.drive.web.utils.ReflectUtil
import io.github.mellivorines.cloud.drive.web.utils.type.FileTypeDefiner
import java.util.stream.Collectors


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/30
 */
/**
 * 文件类型匹配器
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
object FileTypeContext {
    private var fileTypeDefinerList: MutableList<FileTypeDefiner> = ArrayList()

    init {
        val allSubTypeOfFileTypeDefiner: Set<Class<*>> = ReflectUtil.getAllSubTypeOf(
            FileTypeDefiner::class.java, "io.github.mellivorines.cloud.drive.web.file.type"
        )
        if (allSubTypeOfFileTypeDefiner.isNotEmpty()) {
            allSubTypeOfFileTypeDefiner.stream().forEach { subTypeOfFileTypeDefinerClass: Class<*> ->
                val fileTypeDefiner =
                    ReflectUtil.createInstance(subTypeOfFileTypeDefinerClass) as FileTypeDefiner
                fileTypeDefinerList.add(fileTypeDefiner)
            }
        }
    }

    /**
     * 通过文件名称获取文件类型标识
     *
     * @param filename
     * @return
     */
    fun getFileTypeCode(filename: String): Int {
        if (fileTypeDefinerList.isEmpty()) {
            throw RuntimeException("获取文件类型失败")
        }
        fileTypeDefinerList = fileTypeDefinerList.stream()
            .sorted(Comparator.comparingInt { obj: FileTypeDefiner -> obj.order }.reversed())
            .collect(Collectors.toList())
        for (fileTypeDefiner in fileTypeDefinerList) {
            if (fileTypeDefiner.isMatched(filename)) {
                return fileTypeDefiner.fileTypeCode
            }
        }
        throw RuntimeException("获取文件类型失败")
    }
}

