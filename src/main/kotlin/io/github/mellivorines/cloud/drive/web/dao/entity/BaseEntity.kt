package io.github.mellivorines.cloud.drive.web.dao.entity

import io.swagger.v3.oas.annotations.media.Schema
import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@MappedSuperclass
interface BaseEntity {
    /**
     *  创建时间 */
    @Column(name = "create_time")
    @get:Schema(description = " 创建时间 ")
    val createTime: LocalDateTime

    /**
     *  更新时间 */
    @Column(name = "update_time")
    @get:Schema(description = " 更新时间 ")
    val updateTime: LocalDateTime
}