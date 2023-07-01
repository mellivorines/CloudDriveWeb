package io.github.mellivorines.clouddriveweb.dao.repository

import io.github.mellivorines.clouddriveweb.dao.entity.ShareFile
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

/**
 * <p>
 * ShareFileRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Repository
interface ShareFileRepository : KRepository<ShareFile, Long> {

}

