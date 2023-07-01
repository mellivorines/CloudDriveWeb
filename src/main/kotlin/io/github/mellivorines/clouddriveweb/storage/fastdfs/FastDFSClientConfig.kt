package io.github.mellivorines.clouddriveweb.storage.fastdfs

import com.github.tobato.fastdfs.domain.conn.ConnectionPoolConfig
import com.github.tobato.fastdfs.domain.conn.FdfsConnectionPool
import com.github.tobato.fastdfs.domain.conn.TrackerConnectionManager
import com.sun.jndi.ldap.pool.PooledConnectionFactory
import org.apache.commons.collections.CollectionUtils
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableMBeanExport
import org.springframework.jmx.support.RegistrationPolicy
import java.io.Serializable
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
@SpringBootConfiguration
@ComponentScan(value = ["com.github.tobato.fastdfs.service", "com.github.tobato.fastdfs.domain"]) // 解决jmx 重复注册bean
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@ConfigurationProperties(prefix = "rpan.storage.fdfs")
@ConditionalOnProperty(
    prefix = "rpan.storage.processor",
    name = ["type"],
    havingValue = "com.rubin.rpan.storage.fastdfs.FastDFSStorageProcessor"
)
class FastDFSClientConfig : Serializable {
    private var connectTimeout = 600
    private var trackerList: List<String> = mutableListOf()
    fun getConnectTimeout(): Int {
        return connectTimeout
    }

    fun setConnectTimeout(connectTimeout: Int) {
        this.connectTimeout = connectTimeout
    }

    fun getTrackerList(): List<String> {
        return trackerList
    }

    fun setTrackerList(trackerList: List<String>) {
        this.trackerList = trackerList
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as FastDFSClientConfig
        return connectTimeout == that.connectTimeout && trackerList == that.trackerList
    }

    override fun hashCode(): Int {
        return Objects.hash(connectTimeout, trackerList)
    }

    override fun toString(): String {
        return "FastDFSClientConfig{" +
                "connectTimeout=" + connectTimeout +
                ", trackerList=" + trackerList +
                '}'
    }

    @Bean("connectionPoolConfig")
    fun connectionPoolConfig(): ConnectionPoolConfig {
        return ConnectionPoolConfig()
    }

    @Bean("pooledConnectionFactory")
    fun pooledConnectionFactory(): PooledConnectionFactory {
        val pooledConnectionFactory = PooledConnectionFactory()
        pooledConnectionFactory.setConnectTimeout(connectTimeout)
        return pooledConnectionFactory
    }

    @Bean(value = ["fdfsConnectionPool"])
    fun fdfsConnectionPool(
        connectionPoolConfig: ConnectionPoolConfig,
        pooledConnectionFactory: PooledConnectionFactory
    ): FdfsConnectionPool {
        return FdfsConnectionPool(pooledConnectionFactory, connectionPoolConfig)
    }

    @Bean(value = ["trackerConnectionManager"])
    fun trackerConnectionManager(fdfsConnectionPool: FdfsConnectionPool?): TrackerConnectionManager {
        val trackerConnectionManager = TrackerConnectionManager(fdfsConnectionPool)
        if (CollectionUtils.isEmpty(trackerList)) {
            throw RuntimeException("the tracker list can not be enpty!<rpan.storage.fdfs.tracker-list>")
        }
        trackerConnectionManager.setTrackerList(trackerList)
        return trackerConnectionManager
    }

    companion object {
        private const val serialVersionUID = -7298184878964452046L
    }
}
