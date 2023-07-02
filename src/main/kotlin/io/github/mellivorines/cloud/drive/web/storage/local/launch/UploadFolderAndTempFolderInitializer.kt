package io.github.mellivorines.cloud.drive.web.storage.local.launch

import io.github.mellivorines.cloud.drive.web.storage.local.config.LocalStorageConfig
import io.github.mellivorines.cloud.drive.web.utils.FileUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component


/**
 * @Description:初始化上传文件跟目录和临时文件目录处理器
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
@Component(value = "uploadFolderAndTempFolderInitializer")
@ConditionalOnProperty(
    prefix = "cloud.drive.web.storage.processor",
    name = ["type"],
    havingValue = "io.github.mellivorines.cloud.drive.web.storage.local.LocalStorageProcessor"
)
class UploadFolderAndTempFolderInitializer : CloudDriveWebLaunchedProcessor {
    @Autowired
    @Qualifier(value = "localStorageConfig")
    private val localStorageConfig: LocalStorageConfig? = null

    /**
     * 处理初始化任务
     */
    override fun process() {
        FileUtil.createFolder(FileUtil.generateTempFolderPath(localStorageConfig!!.rootFilePath))
        log.info("临时目录初始化完成！")
    }

    companion object {
        private val log = LoggerFactory.getLogger(CloudDriveWebLaunchedProcessor::class.java)
    }
}
