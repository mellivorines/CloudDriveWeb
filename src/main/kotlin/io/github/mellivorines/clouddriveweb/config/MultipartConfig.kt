package io.github.mellivorines.clouddriveweb.config

import jakarta.servlet.MultipartConfigElement
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/28
 */
@Configuration
class MultipartConfig {

    /**
     * 文件上传临时路径
     */
    @Bean
    fun multipartConfigElement(): MultipartConfigElement? {
        val factory = MultipartConfigFactory()
        val location = System.getProperty("user.dir") + "/CloudDrive/temp"
        val tmpFile = File(location)
        if (!tmpFile.exists()) {
            tmpFile.mkdirs()
        }
        factory.setLocation(location)
        return factory.createMultipartConfig()
    }
}