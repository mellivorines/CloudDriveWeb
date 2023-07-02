package io.github.mellivorines.cloud.drive.web.schedule

import io.github.mellivorines.cloud.drive.web.utils.DateUtil.beforeDays
import io.github.mellivorines.cloud.drive.web.utils.FileUtil.delete
import io.github.mellivorines.cloud.drive.web.utils.FileUtil.deleteChunks
import io.github.mellivorines.cloud.drive.web.utils.FileUtil.getMd5FromTempFilename
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */

class ExpireTempFileCleaner(tempFolderPath: String, expireDays: Int) : CloudDriveWebScheduleTask {
    private val tempFolderPath: String
    private val expireDays: Int

    init {
        this.tempFolderPath = tempFolderPath
        this.expireDays = expireDays
    }

    /**
     * 获取执行器名称
     *
     * @return
     */
    override val name: String
        get() =  "过期文件清理任务"

    override fun run() {
        val tempFolder = File(tempFolderPath)
        if (!tempFolder.exists()) {
            log.error("临时文件目录不存在！路径为：$tempFolderPath")
        }
        if (!tempFolder.isDirectory) {
            log.error("非法临时文件目录！路径为：$tempFolderPath")
        }
        log.info(name + "开始！")
        val tempFiles = tempFolder.listFiles()
        val deadlineDate = beforeDays(expireDays)
        if (tempFiles != null) {
            for (i in tempFiles.indices) {
                val tempFile = tempFiles[i]
                if (Date(tempFile.lastModified()).before(deadlineDate)) {
                    delete(tempFile)
                    deleteChunks(getMd5FromTempFilename(tempFile.name))
                    log.info("清理了文件：" + tempFile.path)
                }
            }
        }
        log.info(name + "结束！")
    }

    companion object {
        private val log = LoggerFactory.getLogger(ExpireTempFileCleaner::class.java)
    }
}

