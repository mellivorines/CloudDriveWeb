package io.github.mellivorines.clouddriveweb.storage.core

import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
/**
 * 存储处理器选择器
 */
@Component(value = "storageProcessorSelector")
class StorageProcessorSelector : ApplicationContextAware {
    private lateinit var applicationContext: ApplicationContext
    private lateinit var storageProcessor: StorageProcessor

    @Value("\${rpan.storage.processor.type}")
    private lateinit var storageProcessorType: Class<out StorageProcessor>

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
        if (Objects.isNull(storageProcessorType)) {
            throw RuntimeException("the storage processor type can not be null!<rpan.storage.processor.type>")
        }
        storageProcessor = this.applicationContext.getBean(storageProcessorType)
        if (Objects.isNull(storageProcessor)) {
            throw RuntimeException("the storage processor type:" + storageProcessorType.simpleName + " can not be found!")
        }
    }

    /**
     * 选择StorageProcessor实现类
     *
     * @return
     */
    fun select(): StorageProcessor {
        return storageProcessor
    }
}
