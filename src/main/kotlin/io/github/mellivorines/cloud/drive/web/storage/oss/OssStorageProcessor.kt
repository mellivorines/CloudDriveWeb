package io.github.mellivorines.cloud.drive.web.storage.oss

import com.aliyun.oss.OSSClient
import com.aliyun.oss.model.CompleteMultipartUploadRequest
import com.aliyun.oss.model.InitiateMultipartUploadRequest
import com.aliyun.oss.model.PartETag
import com.aliyun.oss.model.UploadPartRequest
import io.github.mellivorines.cloud.drive.web.cache.core.Cache
import io.github.mellivorines.cloud.drive.web.cache.local.LocalCache
import io.github.mellivorines.cloud.drive.web.storage.core.StorageProcessor
import io.github.mellivorines.cloud.drive.web.utils.DateUtil
import io.github.mellivorines.cloud.drive.web.utils.FileUtil
import io.github.mellivorines.cloud.drive.web.utils.UUIDUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
@Component(value = "ossStorageProcessor")
@ConditionalOnProperty(
    prefix = "cloud.drive.web.storage.processor",
    name = ["type"],
    havingValue = "io.github.mellivorines.cloud.drive.web.storage.oss.OssStorageProcessor"
)
class OssStorageProcessor : StorageProcessor {
    @Autowired
    @Qualifier(value = "ossClient")
    private lateinit var ossClient: OSSClient

    @Value("\${cloud.drive.web.storage.oss.bucket-name}")
    private lateinit var bucketName: String

    @Value("\${cloud.drive.web.storage.oss.auto-create-bucket:true}")
    private var autoCreateBucket: Boolean = true
    private val cache: Cache = LocalCache()
    private val LOCK = Any()

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
        checkBucket()
        val filePath = getFilePath(name)
        ossClient!!.putObject(bucketName, filePath, inputStream)
        return filePath
    }


    /**
     * 文件分片存储
     * 注意：分片上传支持并发随机上传分片
     *
     * @param inputStream 文件输入流
     * @param md5         文件唯一标识
     * @param chunks      分片总数 注意：本模式分片总数不得大于10000片
     * @param chunk       当前分片下标
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
        checkBucket()
        if (chunks > TEN_THOUSAND_INT) {
            throw RuntimeException("分片数超过了限制，分片数不得大于" + TEN_THOUSAND_INT)
        }
        synchronized(LOCK) {
            if (Objects.isNull(cache[md5])) {
                InitiateMultipartUpload(md5, name)
            }
        }
        doUploadMultipart(inputStream, md5, name, chunk, chunkSize)
        if (FileUtil.addChunkAndCheckAllDone(md5, chunks)) {
            completeMultipart(md5, name)
            val filePathKey = getFilePathKey(md5, name)
            val filePath = cache[filePathKey].toString()
            cache.delete(md5)
            cache.delete(filePathKey)
            cache.delete(getPartETagKey(md5))
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
    @Throws(IOException::class)
    override fun read2OutputStream(filePath: String, outputStream: OutputStream) {
        val ossObject = ossClient!!.getObject(bucketName, filePath)
        FileUtil.writeStreamToStreamNormal(ossObject.objectContent, outputStream)
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun delete(filePath: String) {
        ossClient!!.deleteObject(bucketName, filePath)
    }
    /*************************************************************私有 */
    /**
     * 生成文件存储路径
     *
     * @param name
     * @return
     */
    private fun getFilePath(name: String): String {
        return StringBuffer(DateUtil.todayDayString).append(SLASH).append(UUIDUtil.uUID)
            .append(FileUtil.getFileSuffix(name)).toString()
    }

    /**
     * 生成文件路径缓存key
     *
     * @param md5
     * @param name
     * @return
     */
    private fun getFilePathKey(md5: String, name: String): String {
        return StringBuffer(md5).append(SLASH).append(name).toString()
    }

    /**
     * 生成tag的key
     *
     * @param md5
     * @return
     */
    private fun getPartETagKey(md5: String): String {
        return StringBuffer(md5).append(SLASH).append(TAG).toString()
    }

    /**
     * 初始化分片请求 缓存分片id
     *
     * @param md5
     * @param name
     */
    private fun InitiateMultipartUpload(md5: String, name: String) {
        val filePath = getFilePath(name)
        val request = InitiateMultipartUploadRequest(bucketName, filePath)
        val initiateMultipartUploadResult = ossClient.initiateMultipartUpload(request)
        cache.put(md5, initiateMultipartUploadResult.uploadId)
        cache.put(getFilePathKey(md5, name), filePath)
        cache.put(getPartETagKey(md5), CopyOnWriteArrayList<Any>())
    }

    /**
     * 上传分片文件
     *
     * @param inputStream
     * @param md5
     * @param name
     * @param chunk
     * @param chunkSize
     */
    private fun doUploadMultipart(inputStream: InputStream, md5: String, name: String, chunk: Int, chunkSize: Long) {
        val uploadPartRequest = UploadPartRequest()
        uploadPartRequest.bucketName = bucketName
        uploadPartRequest.key = cache[getFilePathKey(md5, name)].toString()
        uploadPartRequest.uploadId = cache[md5].toString()
        uploadPartRequest.inputStream = inputStream
        // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
        uploadPartRequest.partSize = chunkSize
        // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
        uploadPartRequest.partNumber = chunk + ONE_INT
        // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
        val uploadPartResult = ossClient.uploadPart(uploadPartRequest)
        // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
        (cache[getPartETagKey(md5)] as MutableList<PartETag>).add(uploadPartResult.partETag)
    }

    /**
     * 完成分片上传
     *
     * @param md5
     * @param name
     */
    private fun completeMultipart(md5: String, name: String) {
        val filePath = cache[getFilePathKey(md5, name)].toString()
        val uploadId = cache[md5].toString()
        val partETags = cache[getPartETagKey(md5)] as List<PartETag>
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        val completeMultipartUploadRequest = CompleteMultipartUploadRequest(bucketName, filePath, uploadId, partETags)
        // 完成上传。
        ossClient.completeMultipartUpload(completeMultipartUploadRequest)
    }

    /**
     * 校验并根据需要创建bucket
     */
    private fun checkBucket() {
        val bucketExist = ossClient.doesBucketExist(bucketName)
        if (!bucketExist && autoCreateBucket) {
            ossClient.createBucket(bucketName)
        }
        if (!bucketExist && !autoCreateBucket) {
            throw RuntimeException("the bucket $bucketName is not available!")
        }
    }

    companion object {
        private const val SLASH = "/"
        private const val TAG = "PartETag"
        private const val TEN_THOUSAND_INT = 10000
        private const val ONE_INT = 1
    }
}
