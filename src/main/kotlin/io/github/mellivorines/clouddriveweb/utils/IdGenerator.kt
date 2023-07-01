package io.github.mellivorines.clouddriveweb.utils

import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


/**
 * @Description:雪花算法id生成器
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */

class IdGenerator {
    init {
        workerId = machineNum and maxWorkerId
        dataCenterId = machineNum and maxDataCenterId
        sequence = 0L
    }

    companion object {
        /**
         * 工作id 也就是机器id
         */
        private var workerId: Long = 0

        /**
         * 数据中心id
         */
        private var dataCenterId: Long = 0

        /**
         * 序列号
         */
        private var sequence: Long = 0

        /**
         * 初始时间戳
         */
        private const val startTimestamp = 1288834974657L

        /**
         * 工作id长度为5位
         */
        private const val workerIdBits = 5L

        /**
         * 数据中心id长度为5位
         */
        private const val dataCenterIdBits = 5L

        /**
         * 工作id最大值
         */
        private const val maxWorkerId = -1L xor (-1L shl workerIdBits.toInt())

        /**
         * 数据中心id最大值
         */
        private const val maxDataCenterId = -1L xor (-1L shl dataCenterIdBits.toInt())

        /**
         * 序列号长度
         */
        private const val sequenceBits = 12L

        /**
         * 序列号最大值
         */
        private const val sequenceMask = -1L xor (-1L shl sequenceBits.toInt())

        /**
         * 工作id需要左移的位数，12位
         */
        private const val workerIdShift = sequenceBits

        /**
         * 数据id需要左移位数 12+5=17位
         */
        private const val dataCenterIdShift = sequenceBits + workerIdBits

        /**
         * 时间戳需要左移位数 12+5+5=22位
         */
        private const val timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits

        /**
         * 上次时间戳，初始值为负数
         */
        private var lastTimestamp = -1L
        private val machineNum: Long
            /**
             * 获取机器编号
             *
             * @return
             */
            get() {
                val machinePiece: Long
                val sb = StringBuilder()
                var e: Enumeration<NetworkInterface>? = null
                try {
                    e = NetworkInterface.getNetworkInterfaces()
                } catch (e1: SocketException) {
                    e1.printStackTrace()
                }
                while (e!!.hasMoreElements()) {
                    val ni = e.nextElement()
                    sb.append(ni.toString())
                }
                machinePiece = sb.toString().hashCode().toLong()
                return machinePiece
            }

        //下一个ID生成算法
        @Synchronized
        fun nextId(): Long {
            var timestamp = timeGen()
            // 获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
            if (timestamp < lastTimestamp) {
                System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp)
                throw RuntimeException(
                    String.format(
                        "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp
                    )
                )
            }

            // 获取当前时间戳如果等于上次时间戳
            // 说明：还处在同一毫秒内，则在序列号加1；否则序列号赋值为0，从0开始。
            // 0 - 4095
            if (lastTimestamp == timestamp) {
                sequence = sequence + 1 and sequenceMask
                if (sequence == 0L) {
                    timestamp = tilNextMillis(lastTimestamp)
                }
            } else {
                sequence = 0
            }
            //将上次时间戳值刷新
            lastTimestamp = timestamp
            /**
             * 返回结果：
             * (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
             * (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
             * (workerId << workerIdShift) 表示将工作id左移相应位数
             * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
             * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
             */
            return timestamp - startTimestamp shl timestampLeftShift.toInt() or
                    (dataCenterId shl dataCenterIdShift.toInt()) or
                    (workerId shl workerIdShift.toInt()) or
                    sequence
        }

        /**
         * 获取时间戳，并与上次时间戳比较
         *
         * @param lastTimestamp
         * @return
         */
        private fun tilNextMillis(lastTimestamp: Long): Long {
            var timestamp = timeGen()
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen()
            }
            return timestamp
        }

        /**
         * 获取系统时间戳
         *
         * @return
         */
        private fun timeGen(): Long {
            return System.currentTimeMillis()
        }
    }
}
