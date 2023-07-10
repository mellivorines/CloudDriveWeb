package io.github.mellivorines.cloud.drive.web.utils

import com.google.common.base.Splitter
import org.apache.commons.lang3.StringUtils
import java.util.stream.Collectors


/**
 * @Description:字符串和集合转换工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object StringListUtil {
    private const val COMMON_SEPARATOR = "__,__"

    /**
     * 字符串分隔成Integer集合
     *
     * @param origin
     * @return
     */
    fun string2IntegerList(origin: String?): List<Int> {
        return Splitter.on(COMMON_SEPARATOR).splitToList(origin!!).stream().map { s: String? ->
            Integer.valueOf(
                s
            )
        }.collect(Collectors.toList())
    }

    /**
     * 字符串分隔成Long集合
     *
     * @param origin
     * @return
     */
    fun string2LongList(origin: String): List<Long> {
        return Splitter.on(COMMON_SEPARATOR).splitToList(origin).stream().map { s: String? ->
            java.lang.Long.valueOf(
                s
            )
        }.collect(Collectors.toList())
    }

    /**
     * Long集合按照统一分割符拼装字符串
     *
     * @param ids
     * @return
     */
    private fun longListToString(vararg ids: Long?): String {
        return StringUtils.join(listOf(*ids), COMMON_SEPARATOR)
    }

    /**
     * Long集合按照统一分割符拼装字符串
     *
     * @param ids
     * @return
     */
    fun longListToString(ids: Collection<Long>): String {
        val idArr = arrayOfNulls<Long>(ids.size)
        return longListToString(*idArr)
    }
}

