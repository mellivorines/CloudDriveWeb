package io.github.mellivorines.cloud.drive.web.storage.local

import io.github.mellivorines.cloud.drive.web.storage.core.StorageProcessor
import io.github.mellivorines.cloud.drive.web.storage.local.config.LocalStorageConfig
import io.github.mellivorines.cloud.drive.web.utils.FileUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */

@Component(value = "localStorageProcessor")
@ConditionalOnProperty(
    prefix = "cloud.drive.web.storage.processor",
    name = ["type"],
    havingValue = "io.github.mellivorines.cloud.drive.web.storage.local.LocalStorageProcessor"
)
class LocalStorageProcessor : StorageProcessor {
    @Autowired
    @Qualifier(value = "localStorageConfig")
    private val localStorageConfig: LocalStorageConfig? = null

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
    override fun store(inputStream: InputStream, size: Long, name: String): String {
        val filePath: String = FileUtil.generateFilePath(localStorageConfig!!.rootFilePath, name)
        val targetFile = File(filePath)
        if (!targetFile.parentFile.exists()) {
            targetFile.parentFile.mkdirs()
        }
        targetFile.createNewFile()
        FileUtil.writeStreamToFile(inputStream, FileOutputStream(targetFile), size)
        return filePath
    }

    /**
     * 文件分片存储
     * 注意：此方法可以保证并发分片上传不出问题
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
    override fun storeWithChunk(
        inputStream: InputStream,
        md5: String,
        chunks: Int,
        chunk: Int,
        size: Long,
        chunkSize: Long,
        name: String
    ): String? {
        val tempFilePath: String = FileUtil.generateTempFilePath(localStorageConfig!!.tempPath, name, md5)
        FileUtil.writeWithChunk(
            tempFilePath,
            size,
            inputStream,
            chunkSize,
            chunks,
            chunk
        )
        if (FileUtil.addChunkAndCheckAllDone(md5, chunks)) {
            val filePath: String = FileUtil.generateFilePath(localStorageConfig.rootFilePath, name)
            FileUtil.moveFile(tempFilePath, filePath)
            return filePath
        }
        return null
    }

    /**
     * 读取文件为输入流
     *
     * @param filePath     文件路径
     * @param outputStream 输出流
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun read2OutputStream(filePath: String, outputStream: OutputStream) {
        val file = File(filePath)
        FileUtil.writeFileToStream(FileInputStream(file), outputStream, file.length())
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun delete(filePath: String) {
        FileUtil.delete(File(filePath))
    }
}

