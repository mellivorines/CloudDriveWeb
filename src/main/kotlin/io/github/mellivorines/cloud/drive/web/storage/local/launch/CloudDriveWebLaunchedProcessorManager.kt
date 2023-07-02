package io.github.mellivorines.cloud.drive.web.storage.local.launch

import io.github.mellivorines.cloud.drive.web.storage.core.StorageProcessor
import io.github.mellivorines.cloud.drive.web.storage.local.LocalStorageProcessor
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.function.Consumer


/**
 * @Description:初始化处理器
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
@Component(value = "cloudDriveWebInitProcessorManager")
@ConditionalOnProperty(
    prefix = "cloud.drive.web.storage.processor",
    name = ["type"],
    havingValue = "io.github.mellivorines.cloud.drive.web.storage.local.LocalStorageProcessor"
)
class CloudDriveWebLaunchedProcessorManager : InitializingBean {
    @Autowired
    private val cloudDriveWebLaunchedProcessors: List<CloudDriveWebLaunchedProcessor?>? = null

    @Value("\${cloud.drive.web.storage.processor.type}")
    private val storageProcessorType: Class<out StorageProcessor?>? = null

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        if (CollectionUtils.isNotEmpty(cloudDriveWebLaunchedProcessors) && storageProcessorType == LocalStorageProcessor::class.java) {
            cloudDriveWebLaunchedProcessors!!.forEach(Consumer { obj: CloudDriveWebLaunchedProcessor? -> obj!!.process() })
        }
    }
}
