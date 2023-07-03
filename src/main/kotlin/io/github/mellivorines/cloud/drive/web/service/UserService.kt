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
class UserService(private val userRepository: UserRepository, private val cacheManager: CacheManager) {
    /**
     * 用户注册
     *
     * @param [userInput]用户信息
     * @return [ResultModel]返回结果
     */
    fun register(userInput: UserInput): ResultModel {
        val user = assembleUser(userInput)
        val save = userRepository.save(user)
//        createUserRootFolder(user)
        return success(save)
    }

    /**
     * 生存盐和密码加密
     *
     * @param [userInput]用户信息
     * @return [UserInput] 用户信息
     */
    private fun assembleUser(userInput: UserInput): UserInput {
        val mySalt = PasswordUtil.salt
        userInput.salt = mySalt
        val dbPassword = PasswordUtil.encryptPassword(mySalt, userInput.password)
        userInput.password = dbPassword
        return userInput
    }

    /**
     * 用户登陆
     *
     * @param [userName]用户名
     * @param [password]密码
     * @return [ResultModel]返回结果
     */
    fun login(userName: String, password: String): ResultModel {
        val user = checkUsernameAndPassword(userName, password)
        return generateAndSaveToken(user)
    }

    /**
     * 生成token
     *
     * @param [user]用户信息
     * @return [ResultModel]返回结果
     */
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

    /**
     * 校验用户名和密码
     *
     * @param [userName]用户名
     * @param [password]密码
     * @return [User]用户信息
     */
    private fun checkUsernameAndPassword(userName: String, password: String): User {
        val findByUserName = userRepository.findByUsername(userName)
        if (ObjectUtils.isEmpty(findByUserName)) {
            throw BizException(msg = "用户名错误")
        }

        if (password != PasswordUtil.encryptPassword(findByUserName.salt, findByUserName.password)) {
            throw BizException(msg = "密码错误")
        }
        return findByUserName
    }


    /**
     * 用户退出登陆
     *
     * @param [userId]用户ID
     * @return [ResultModel]返回结果
     */
    fun exit(userId: String): ResultModel {
        try {
            cacheManager.delete(CommonConstant.USER_LOGIN_PREFIX + userId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw BizException(msg = "登出失败")
        }
        return success("退出成功！")
    }

    /**
     * 获取用户详情
     *
     * @param [userId]用户ID
     * @return [ResultModel]返回结果
     */
    fun info(userId: String): ResultModel {
        val findById = userRepository.findById(userId)
        return success(findById)
    }
}