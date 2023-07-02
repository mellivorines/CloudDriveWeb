package io.github.mellivorines.cloud.drive.web.schedule

import io.github.mellivorines.cloud.drive.web.cache.core.Cache
import io.github.mellivorines.cloud.drive.web.cache.local.LocalCache
import io.github.mellivorines.cloud.drive.web.utils.UUIDUtil.uUID
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component
import java.util.*


/**
 * @Description:定时任务调度器
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
@Component(value = "cloudDriveWebScheduleManager")
class CloudDriveWebScheduleManager {
    @Autowired
    @Qualifier(value = "threadPoolTaskScheduler")
    private lateinit var  threadPoolTaskScheduler: ThreadPoolTaskScheduler
    private var  cache: Cache = LocalCache()

    /**
     * 启动定时任务
     *
     * @param cloudDriveWebScheduleTask
     * @param cron
     */
    fun startTask(cloudDriveWebScheduleTask: CloudDriveWebScheduleTask, cron: String): String {
        val scheduledFuture = cloudDriveWebScheduleTask.let {
            threadPoolTaskScheduler.schedule(
                it, CronTrigger(
                    cron
                )
            )
        }
        val key = uUID
        val cloudDriveWebScheduleTaskHolder =
            scheduledFuture?.let { CloudDriveWebScheduleTaskHolder(cloudDriveWebScheduleTask, it) }
        if (cloudDriveWebScheduleTaskHolder != null) {
            cache.put(key, cloudDriveWebScheduleTaskHolder)
        }
        log.info("{}启动成功！唯一标识为{}", cloudDriveWebScheduleTask.name, key)
        return key
    }

    /**
     * 停止定时任务
     *
     * @param key
     */
    fun stopTask(key: String?) {
        if (StringUtils.isBlank(key)) {
            return
        }
        val cacheValue = cache[key!!]
        if (Objects.isNull(cacheValue)) {
            return
        }
        (cacheValue as CloudDriveWebScheduleTaskHolder?)!!.scheduledFuture.cancel(true)
        log.info("{}停止成功！唯一标识为{}", cacheValue!!.getCloudDriveWebScheduleTask().name, key)
    }

    /**
     * 变更任务时间
     *
     * @param key
     * @param cron
     * @return
     */
    fun changeTask(key: String, cron: String): String {
        if (StringUtils.isBlank(key)) {
            throw RuntimeException(key + "标识不存在")
        }
        val cacheValue = cache[key]
        if (Objects.isNull(cacheValue)) {
            throw RuntimeException(key + "标识不存在")
        }
        stopTask(key) // 先停止，在开启.
        return startTask((cacheValue as CloudDriveWebScheduleTaskHolder).getCloudDriveWebScheduleTask(), cron)
    }

    companion object {
        private val log = LoggerFactory.getLogger(CloudDriveWebScheduleManager::class.java)
    }
}

