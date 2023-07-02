package io.github.mellivorines.cloud.drive.web.schedule


/**
 * @Description:自定义业务逻辑执行器接口
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
interface CloudDriveWebScheduleTask : Runnable {
    /**
     * 获取执行器名称
     *
     * @return
     */
    val name: String?
}

