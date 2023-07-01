package io.github.mellivorines.clouddriveweb.storage.fastdfs

import ch.qos.logback.core.util.FileUtil
import com.github.tobato.fastdfs.domain.fdfs.StorePath
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray
import com.github.tobato.fastdfs.service.DefaultAppendFileStorageClient
import com.github.tobato.fastdfs.service.FastFileStorageClient
import io.github.mellivorines.clouddriveweb.cache.core.Cache
import io.github.mellivorines.clouddriveweb.cache.local.LocalCache
import io.github.mellivorines.clouddriveweb.storage.core.StorageProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
@Component(value = "fastDFSStorageProcessor")
@ConditionalOnProperty(
    prefix = "rpan.storage.processor",
    name = ["type"],
    havingValue = "com.rubin.rpan.storage.fastdfs.FastDFSStorageProcessor"
)
class FastDFSStorageProcessor : StorageProcessor {
    @Autowired
    private lateinit var  fastFileStorageClient: FastFileStorageClient

    @Autowired
    private lateinit val defaultAppendFileStorageClient: DefaultAppendFileStorageClient

    @Value("\${rpan.storage.fdfs.group:group1}")
    private lateinit var  group: String
    private var  cache: Cache = LocalCache()

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
    override fun store(inputStream: InputStream?, size: Long?, name: String?): String {
        val storePath: StorePath =
            fastFileStorageClient.uploadFile(group, inputStream, size, FileUtil.getFileExtName(name))
        return storePath.getFullPath()
    }

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
    override fun storeWithChunk(
        inputStream: InputStream?,
        md5: String?,
        chunks: Int?,
        chunk: Int?,
        size: Long?,
        chunkSize: Long?,
        name: String?
    ): String? {
        val fileExtName: String = FileUtil.getFileExtName(name)
        val storePath: StorePath
        if (chunk == ZERO_INT) {
            storePath = defaultAppendFileStorageClient.uploadAppenderFile(group, inputStream, chunkSize, fileExtName)
            cache.put(md5, storePath.getPath())
        } else {
            val offset: Long
            offset = if (chunk == chunks - 1) {
                size - chunkSize
            } else {
                chunk * chunkSize
            }
            defaultAppendFileStorageClient.modifyFile(
                group,
                java.lang.String.valueOf(cache.get(md5)),
                inputStream,
                chunkSize,
                offset
            )
        }
        if (FileUtil.addChunkAndCheckAllDone(md5, chunks)) {
            val filePath = group.toString() + SLASH + java.lang.String.valueOf(cache.get(md5))
            cache.delete(md5)
            return filePath
        }
        return null
    }

    /**
     * 读取文件进输出流
     *
     * @param filePath     文件路径
     * @param outputStream 输出流
     * @throws IOException
     */
    override fun read2OutputStream(filePath: String?, outputStream: OutputStream?) {
        val group = filePath.substring(ZERO_INT, filePath.indexOf(SLASH))
        val path = filePath.substring(filePath.indexOf(SLASH) + ONE_INT)
        val downloadByteArray = DownloadByteArray()
        val bytes: ByteArray = fastFileStorageClient.downloadFile(group, path, downloadByteArray)
        outputStream.write(bytes)
        outputStream.flush()
        outputStream.close()
    }


    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun delete(filePath: String?) {
        fastFileStorageClient.deleteFile(filePath)
    }

    companion object {
        private const val ZERO_INT = 0
        private const val ONE_INT = 1
        private const val SLASH = "/"
    }
}
