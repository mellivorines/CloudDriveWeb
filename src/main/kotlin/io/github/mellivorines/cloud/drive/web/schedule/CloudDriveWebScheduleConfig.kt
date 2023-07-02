package io.github.mellivorines.cloud.drive.web.schedule

import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */

@SpringBootConfiguration
class CloudDriveWebScheduleConfig {
    @Bean(value = ["threadPoolTaskScheduler"])
    fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler {
        return ThreadPoolTaskScheduler()
    }
}

