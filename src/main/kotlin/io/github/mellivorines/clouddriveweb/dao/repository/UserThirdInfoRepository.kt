package io.github.mellivorines.clouddriveweb.dao.repository

import io.github.mellivorines.clouddriveweb.dao.entity.UserThirdInfo
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

/**
 * <p>
 * UserThirdInfoRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Repository
interface UserThirdInfoRepository : KRepository<UserThirdInfo, String> {

}

