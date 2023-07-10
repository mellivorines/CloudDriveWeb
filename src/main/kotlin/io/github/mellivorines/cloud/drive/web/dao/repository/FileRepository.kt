package io.github.mellivorines.cloud.drive.web.dao.repository

import io.github.mellivorines.cloud.drive.web.dao.entity.File
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

/**
 * <p>
 * FileRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Repository
interface FileRepository : KRepository<File, String> {
fun findByMd5(md5: String): List<File>?
}

