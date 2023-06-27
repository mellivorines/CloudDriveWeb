package io.github.mellivorines.clouddriveweb.dao.repository

import io.github.mellivorines.clouddriveweb.dao.entity.User
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

/**
 * <p>
 * UserRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-06-22
 */
@Repository
interface UserRepository : KRepository<User, String> {

    fun findByUserNameAndPassword(userName: String, password: String): User?

    fun findByUserName(userName: String): User?
}

