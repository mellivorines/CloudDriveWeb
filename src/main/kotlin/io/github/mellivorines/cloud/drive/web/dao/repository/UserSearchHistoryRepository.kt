package io.github.mellivorines.cloud.drive.web.dao.repository

import io.github.mellivorines.cloud.drive.web.dao.entity.UserSearchHistory
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

/**
 * <p>
 * UserSearchHistoryRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Repository
interface UserSearchHistoryRepository : KRepository<UserSearchHistory, String> {
    fun findByUserIdOrderByUpdateTime(userId: String): List<UserSearchHistory>?
}

