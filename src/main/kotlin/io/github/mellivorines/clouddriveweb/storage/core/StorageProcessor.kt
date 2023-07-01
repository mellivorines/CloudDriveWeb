package io.github.mellivorines.clouddriveweb.storage.core

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * @Description:存储处理器顶级接口
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
interface StorageProcessor {
    /**
     * 文件存储
     *
     * @param inputStream 文件输入流
     * @param size        文件大小
     * @param name        文件原名
     * @return 文件存储路径
     * @throws IOException
     */
    @Throws(IOException::class)
    fun store(inputStream: InputStream?, size: Long?, name: String?): String?

    /**
     * 文件分片存储
     *
     * @param inputStream 文件输入流
     * @param md5         文件唯一标识
     * @param chunks      分片总数
     * @param chunk       当前分片下标 从0开始
     * @param size        文件总大小
     * @param chunkSize   分片文件大小
     * @param name        文件名称
     * @return null 代表未传完 有值代表已经传输完毕
     * @throws IOException
     */
    @Throws(IOException::class)
    fun storeWithChunk(
        inputStream: InputStream?,
        md5: String?,
        chunks: Int?,
        chunk: Int?,
        size: Long?,
        chunkSize: Long?,
        name: String?
    ): String?

    /**
     * 读取文件进输出流
     *
     * @param filePath     文件路径
     * @param outputStream 输出流
     * @throws IOException
     */
    @Throws(IOException::class)
    fun read2OutputStream(filePath: String?, outputStream: OutputStream?)

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Throws(IOException::class)
    fun delete(filePath: String?)
}
