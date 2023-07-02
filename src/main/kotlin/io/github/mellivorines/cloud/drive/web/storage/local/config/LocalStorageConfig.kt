package io.github.mellivorines.cloud.drive.web.storage.local.config

import io.github.mellivorines.cloud.drive.web.utils.FileUtil
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
@ConfigurationProperties(prefix = "cloud.drive.web.storage.local")
@ConditionalOnProperty(
    prefix = "cloud.drive.web.storage.processor",
    name = ["type"],
    havingValue = "io.github.mellivorines.cloud.drive.web.storage.local.LocalStorageProcessor"
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as LocalStorageConfig
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
