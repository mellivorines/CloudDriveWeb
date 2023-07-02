package io.github.mellivorines.cloud.drive.web.utils

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException


/**
 *
 * @param basePath String 项目配置的基础路径
 * @param files Array<MultipartFile>? 文件夹或者文件
 * @return Boolean 是否上传成功
 */
fun saveMultiFile(basePath: String, files: Array<MultipartFile>?): Boolean {
    var flag = false
    var path = basePath
    if (files.isNullOrEmpty()) {
        return false
    }
    if (path.endsWith("/")) {
        path = path.substring(0, path.length - 1)
    }
    for (file in files) {
        val filePath = path + "/" + file.originalFilename
        makeDir(filePath)
        val dest = File(filePath)
        try {
            file.transferTo(dest)
            flag = true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return flag
}

/**
 * 确保目录存在，不存在则创建
 * @param filePath
 */
private fun makeDir(filePath: String) {
    if (filePath.lastIndexOf('/') > 0) {
        val dirPath = filePath.substring(0, filePath.lastIndexOf('/'))
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }
}

