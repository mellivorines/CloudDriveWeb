package io.github.mellivorines.cloud.drive.web.service

import io.github.mellivorines.cloud.drive.web.cache.core.CacheManager
import io.github.mellivorines.cloud.drive.web.constants.CommonConstant
import io.github.mellivorines.cloud.drive.web.dao.entity.User
import io.github.mellivorines.cloud.drive.web.dao.input.UserInput
import io.github.mellivorines.cloud.drive.web.dao.repository.UserRepository
import io.github.mellivorines.cloud.drive.web.exception.BizException
import io.github.mellivorines.cloud.drive.web.model.ResultModel
import io.github.mellivorines.cloud.drive.web.model.success
import io.github.mellivorines.cloud.drive.web.utils.JwtUtil
import io.github.mellivorines.cloud.drive.web.utils.PasswordUtil
import org.apache.commons.lang3.ObjectUtils
import org.springframework.stereotype.Service


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@Service
class UserService(private val userRepository: UserRepository,private val cacheManager: CacheManager) {
    fun register(userInput: UserInput): ResultModel {
        val user = assembleUser(userInput)
        val save = userRepository.save(user)
//        createUserRootFolder(user)
        return success(save)
    }

    private fun assembleUser(userInput: UserInput): UserInput {
        val mySalt = PasswordUtil.salt
        userInput.salt = mySalt
        val dbPassword = PasswordUtil.encryptPassword(mySalt, userInput.password)
        userInput.password = dbPassword
        return userInput
    }

    fun login(userName: String, password: String): ResultModel {
        val user = checkUsernameAndPassword(userName, password)
        return generateAndSaveToken(user)
    }

    private fun generateAndSaveToken(user: User): ResultModel {
        val token: String = JwtUtil.generateToken(
            user.username,
            CommonConstant.LOGIN_USER_ID,
            user.userId,
            CommonConstant.ONE_DAY_LONG
        )
        cacheManager.put(CommonConstant.USER_LOGIN_PREFIX + user.userId, token, CommonConstant.ONE_DAY_LONG)
        return success(token)
    }

    private fun checkUsernameAndPassword(userName: String, password: String): User {
        val findByUserName = userRepository.findByUsername(userName)
        if (ObjectUtils.isEmpty(findByUserName)){
            BizException(msg = "用户名错误")
        }

        if (password != PasswordUtil.encryptPassword(findByUserName.salt, findByUserName.password)) {
            BizException(msg = "密码错误")
        }
        return findByUserName
    }
}