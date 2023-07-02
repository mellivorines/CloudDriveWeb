package io.github.mellivorines.cloud.drive.web.storage.oss

import com.aliyun.oss.OSSClient
import org.apache.commons.lang3.StringUtils
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import java.io.Serializable
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/1
 */
@SpringBootConfiguration
@ConfigurationProperties(prefix = "pan.storage.oss")
@ConditionalOnProperty(
    prefix = "cloud.drive.web.storage.processor",
    name = ["type"],
    havingValue = "io.github.mellivorines.cloud.drive.web.storage.oss.OssStorageProcessor"
)
class OssClientConfig : Serializable {
    private var endpoint: String? = null
    private var accessKeyId: String? = null
    private var accessKeySecret: String? = null
    private var bucketName: String? = null
    @Bean(value = ["ossClient"])
    fun ossClient(): OSSClient {
        if (StringUtils.isAnyBlank(endpoint, accessKeyId, accessKeySecret, bucketName)) {
            throw RuntimeException("the oss config is missed!<cloud.drive.web.storage.oss.endpoint><cloud.drive.web.storage.oss.access-key-id><cloud.drive.web.storage.oss.access-key-secret><cloud.drive.web.storage.oss.bucket-name>")
        }
        return OSSClient(endpoint, accessKeyId, accessKeySecret)
    }

    fun getEndpoint(): String? {
        return endpoint
    }

    fun setEndpoint(endpoint: String?) {
        this.endpoint = endpoint
    }

    fun getAccessKeyId(): String? {
        return accessKeyId
    }

    fun setAccessKeyId(accessKeyId: String?) {
        this.accessKeyId = accessKeyId
    }

    fun getAccessKeySecret(): String? {
        return accessKeySecret
    }

    fun setAccessKeySecret(accessKeySecret: String?) {
        this.accessKeySecret = accessKeySecret
    }

    fun getBucketName(): String? {
        return bucketName
    }

    fun setBucketName(bucketName: String?) {
        this.bucketName = bucketName
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as OssClientConfig
        return endpoint == that.endpoint && accessKeyId == that.accessKeyId && accessKeySecret == that.accessKeySecret && bucketName == that.bucketName
    }

    override fun hashCode(): Int {
        return Objects.hash(endpoint, accessKeyId, accessKeySecret, bucketName)
    }

    override fun toString(): String {
        return "OssClientConfig{" +
                "endpoint='" + endpoint + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}'
    }

    companion object {
        private const val serialVersionUID = -5370228155207535548L
    }
}

