package io.github.mellivorines.cloud.drive.web.storage.local.launch

import io.github.mellivorines.cloud.drive.web.schedule.CloudDriveWebScheduleManager
import io.github.mellivorines.cloud.drive.web.schedule.ExpireTempFileCleaner
import io.github.mellivorines.cloud.drive.web.storage.local.config.LocalStorageConfig
import io.github.mellivorines.cloud.drive.web.utils.FileUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
/**
 * 清理过期临时文件任务启动器
 */
@Component(value = "expireTempFileCleanerLauncher")
@ConditionalOnProperty(
    prefix = "cloud.drive.web.storage.processor",
    name = ["type"],
    havingValue = "io.github.mellivorines.cloud.drive.web.storage.local.LocalStorageProcessor"
)
class ExpireTempFileCleanerLauncher : CloudDriveWebLaunchedProcessor {
    @Autowired
    @Qualifier(value = "cloudDriveWebScheduleManager")
    private val cloudDriveWebScheduleManager: CloudDriveWebScheduleManager? = null

    @Autowired
    @Qualifier(value = "localStorageConfig")
    private val localStorageConfig: LocalStorageConfig? = null

    /**
     * 处理初始化任务
     */
    override fun process() {
        cloudDriveWebScheduleManager!!.startTask(
            ExpireTempFileCleaner(
                FileUtil.generateTempFolderPath(localStorageConfig!!.rootFilePath),
                localStorageConfig.intervalDaysForClearingExpiredTempFiles
            ), CRON
        )
    }

    companion object {
        private const val CRON = "1 0 0 * * ? "
    }
}
