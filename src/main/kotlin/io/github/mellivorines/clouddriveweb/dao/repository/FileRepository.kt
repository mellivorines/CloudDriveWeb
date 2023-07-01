package io.github.mellivorines.clouddriveweb.dao.repository

import io.github.mellivorines.clouddriveweb.dao.entity.File
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
interface FileRepository : KRepository<File, Long> {

}

