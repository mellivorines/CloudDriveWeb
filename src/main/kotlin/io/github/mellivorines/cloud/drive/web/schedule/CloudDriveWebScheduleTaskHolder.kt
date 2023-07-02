package io.github.mellivorines.cloud.drive.web.schedule

import java.io.Serializable
import java.util.*
import java.util.concurrent.ScheduledFuture


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */

class CloudDriveWebScheduleTaskHolder : Serializable {

    private lateinit var cloudDriveWebScheduleTask: CloudDriveWebScheduleTask

    @get:JvmName("getCloudDriveWebScheduledFuture")
    @set:JvmName("changeCloudDriveWebScheduledFuture")
    lateinit var scheduledFuture: ScheduledFuture<*>

    constructor(cloudDriveWebScheduleTask: CloudDriveWebScheduleTask, scheduledFuture: ScheduledFuture<*>) {
        this.cloudDriveWebScheduleTask = cloudDriveWebScheduleTask
        this.scheduledFuture = scheduledFuture
    }

    constructor()

    fun getCloudDriveWebScheduleTask(): CloudDriveWebScheduleTask {
        return cloudDriveWebScheduleTask
    }

    fun setCloudDriveWebScheduleTask(cloudDriveWebScheduleTask: CloudDriveWebScheduleTask) {
        this.cloudDriveWebScheduleTask = cloudDriveWebScheduleTask
    }

    fun getScheduledFuture(): ScheduledFuture<*> {
        return scheduledFuture
    }

    fun setScheduledFuture(scheduledFuture: ScheduledFuture<Any>) {
        this.scheduledFuture = scheduledFuture
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CloudDriveWebScheduleTaskHolder
        return cloudDriveWebScheduleTask == that.cloudDriveWebScheduleTask && scheduledFuture == that.scheduledFuture
    }

    override fun hashCode(): Int {
        return Objects.hash(cloudDriveWebScheduleTask, scheduledFuture)
    }

    override fun toString(): String {
        return "CloudDriveWebScheduleTaskHolder{" +
                "cloudDriveWebScheduleTask=" + cloudDriveWebScheduleTask +
                ", scheduledFuture=" + scheduledFuture +
                '}'
    }

    companion object {
        private const val serialVersionUID = 1444488140009722221L
    }
}
