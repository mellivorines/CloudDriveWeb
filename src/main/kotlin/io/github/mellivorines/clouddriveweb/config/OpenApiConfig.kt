package io.github.mellivorines.clouddriveweb.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * @Description: OpenAPI配置
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/23
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI().info(Info().title("CloudDriveWeb").version("0.0.1"))

}