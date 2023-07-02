package io.github.mellivorines.cloud.drive.web.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * @Description:日期工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object DateUtil {
    /**
     * 获取当天的日期字符串的格式常量
     */
    private const val TODAY_DAY_STR_FORMAT = "yyyyMMdd"

    /**
     * 获取当前时间N天后的日期
     *
     * @param days
     * @return
     */
    fun afterDays(days: Int?): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, days!!)
        return calendar.time
    }

    /**
     * 获取特定时间N天后的日期
     *
     * @param date
     * @param days
     * @return
     */
    fun afterDays(date: Date?, days: Int?): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, days!!)
        return calendar.time
    }

    /**
     * 获取当前时间N天前的日期
     *
     * @param days
     * @return
     */
    fun beforeDays(days: Int?): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -days!!)
        return calendar.time
    }

    /**
     * 获取特定时间N天前的日期
     *
     * @param date
     * @param days
     * @return
     */
    fun beforeDays(date: Date?, days: Int?): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -days!!)
        return calendar.time
    }

    val todayDayString: String
        /**
         * 获取当天的日期字符串，按照yyyyMMdd格式返回
         * @return
         */
        get() {
            val simpleDateFormat = SimpleDateFormat(TODAY_DAY_STR_FORMAT)
            return simpleDateFormat.format(Date())
        }
}

