package io.github.mellivorines.cloud.drive.web.dao.repository

import io.github.mellivorines.cloud.drive.web.dao.entity.User
import org.babyfish.jimmer.spring.repository.KRepository
import org.springframework.stereotype.Repository

/**
 * <p>
 * UserRepository 接口
 * </p>
 *
 * @author lilinxi
 * @date 2023-07-01
 */
@Repository
interface UserRepository : KRepository<User, String> {
    fun findByUsername(userName: String): User?
    fun findByUsernameAndQuestionAndAnswer(username: String, question: String, answer: String): User?
    fun findByUserIdAndPassword(userId: String, password: String): User?
}

