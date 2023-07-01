package io.github.mellivorines.clouddriveweb.storage.local.config

import io.github.mellivorines.clouddriveweb.utils.FileUtil
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*


/**
 * @Description:文件基础路径配置类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
@Component(value = "localStorageConfig")
@ConfigurationProperties(prefix = "rpan.storage.local")
@ConditionalOnProperty(
    prefix = "rpan.storage.processor",
    name = ["type"],
    havingValue = "com.rubin.rpan.storage.local.LocalStorageProcessor"
)
class LocalStorageConfig {
    /**
     * 实际存放路径前缀
     */
    final var rootFilePath: String = FileUtil.generateDefaultRootFolderPath()

    /**
     * 临时文件存放路径前缀
     */
    var tempPath: String = FileUtil.generateTempFolderPath(rootFilePath)

    /**
     * 过期文件删除间隔时长（天，即删除最后修改时间在当前时间前几天的过期临时文件）
     */
    var intervalDaysForClearingExpiredTempFiles = SEVEN_INT

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as LocalStorageConfig
        return rootFilePath == that.rootFilePath && tempPath == that.tempPath && intervalDaysForClearingExpiredTempFiles == that.intervalDaysForClearingExpiredTempFiles
    }

    override fun hashCode(): Int {
        return Objects.hash(rootFilePath, tempPath, intervalDaysForClearingExpiredTempFiles)
    }

    override fun toString(): String {
        return "LocalStorageConfig{" +
                "rootFilePath='" + rootFilePath + '\'' +
                ", tempPath='" + tempPath + '\'' +
                ", intervalDaysForClearingExpiredTempFiles=" + intervalDaysForClearingExpiredTempFiles +
                '}'
    }

    companion object {
        private const val SEVEN_INT = 7
    }
}
