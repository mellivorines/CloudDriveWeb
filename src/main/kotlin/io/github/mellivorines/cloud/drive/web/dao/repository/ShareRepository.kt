package io.github.mellivorines.cloud.drive.web.dao.repository

import io.github.mellivorines.cloud.drive.web.dao.entity.Share
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

/**
 * <p>
 * ShareRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Repository
interface ShareRepository : KRepository<Share, String> {

}

