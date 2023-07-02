package io.github.mellivorines.cloud.drive.web.utils.io

import java.io.InputStream


/**
 * @Description:资源读取类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */

object Resources {
    /**
     * 读取资源为输入流
     *
     * @param filePath
     * @return
     */
    fun readFileAsInputStream(filePath: String?): InputStream? {
        return Resources::class.java.classLoader.getResourceAsStream(filePath)
    }
}
