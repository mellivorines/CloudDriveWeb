package io.github.mellivorines.cloud.drive.web.service

import io.github.mellivorines.cloud.drive.web.cache.core.CacheManager
import io.github.mellivorines.cloud.drive.web.constants.CommonConstant
import io.github.mellivorines.cloud.drive.web.dao.entity.User
import io.github.mellivorines.cloud.drive.web.dao.input.UserInput
import io.github.mellivorines.cloud.drive.web.dao.repository.UserRepository
import io.github.mellivorines.cloud.drive.web.enum.ResponseCode
import io.github.mellivorines.cloud.drive.web.exception.BizException
import io.github.mellivorines.cloud.drive.web.model.ResultModel
import io.github.mellivorines.cloud.drive.web.model.fail
import io.github.mellivorines.cloud.drive.web.model.success
import io.github.mellivorines.cloud.drive.web.utils.JwtUtil
import io.github.mellivorines.cloud.drive.web.utils.PasswordUtil
import io.github.mellivorines.cloud.drive.web.utils.UUIDUtil
import org.apache.commons.lang3.ObjectUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@Service
class UserService(private val userRepository: UserRepository, private val cacheManager: CacheManager) {

    @Autowired
    private lateinit var userFileService: UserFileService

    /**
     * 用户注册
     *
     * @param [userInput]用户信息
     * @return [ResultModel]返回结果
     */
    fun register(userInput: UserInput): ResultModel {
        val user = assembleUser(userInput)
        val findByUsername = userRepository.findByUsername(user.username)
        return if (ObjectUtils.isEmpty(findByUsername)) {
            val save = userRepository.save(user)
            createUserRootFolder(save)
            success(save)
        } else {
            fail("用户已经存在!")
            throw BizException(msg = "用户已经存在！")
        }

    }

    private fun createUserRootFolder(user: User) {
        user.userId.let {
            userFileService.createFolder(
                CommonConstant.ZERO_LONG.toString(), CommonConstant.ALL_FILE_CN_STR,
                it
            )
        }
    }

    /**
     * 用户登陆
     *
     * @param [username]用户名
     * @param [password]密码
     * @return [ResultModel]返回结果
     */
    fun login(username: String, password: String): ResultModel {
        val user = checkUsernameAndPassword(username, password)
        return generateAndSaveToken(user)
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


    /**
     * 忘记密码-校验用户名
     *
     * @param [username] 用户名
     * @return [ResultModel]返回结果
     */
    fun checkUsername(username: String): ResultModel {
        val findByUsername = userRepository.findByUsername(username)
        return if (findByUsername == null) {
            fail("没有此用户!")
            throw BizException(msg = "没有此用户!")
        } else {
            success(findByUsername.question)
        }
    }

    /**
     * 忘记密码-校验密保答案
     *
     * @param [username] 用户名
     * @param [question]密保问题
     * @param [answer]密保答案
     * @return [ResultModel] 返回结果
     */
    fun checkAnswer(username: String, question: String, answer: String): ResultModel {
        val findByUsernameAndQuestionAndAnswer =
            userRepository.findByUsernameAndQuestionAndAnswer(username, question, answer)
        if (ObjectUtils.isEmpty(findByUsernameAndQuestionAndAnswer)) {
            throw BizException(msg = "密保答案错误")
        }
        return success(generateAndSaveCheckAnswerToken(username))
    }


    /**
     * 忘记密码-重置密码
     *
     * @param [username] 用户名
     * @param [newPassword]新密码
     * @param [token]token
     * @return [ResultModel]返回结果
     */
    fun resetPassword(username: String, newPassword: String, token: String): ResultModel {
        val findByUsername = userRepository.findByUsername(username)
        return if (findByUsername == null) {
            fail("用户不存在!")
            throw BizException(msg = "用户不存在！")
        } else {
            checkResetPasswordToken(username, token)
            doChangeOrResetPassword(findByUsername, newPassword)
        }
    }

    /**
     * 用户修改密码
     *
     * @param [userId]用户ID
     * @param [password]原始密码
     * @param [newPassword] 新密码
     * @return [ResultModel] 返回结果
     */
    fun changePassword(userId: String, password: String, newPassword: String): ResultModel {
        val findByUserIdAndPassword = userRepository.findByUserIdAndPassword(userId, password)
        if (findByUserIdAndPassword == null) {
            throw BizException(msg = "用户名错误")
        } else {
            if (password != PasswordUtil.encryptPassword(
                    findByUserIdAndPassword.salt,
                    findByUserIdAndPassword.password
                )
            ) {
                throw  BizException(msg = "密码错误")
            }
        }
        return doChangeOrResetPassword(findByUserIdAndPassword, newPassword)

    }

    /**
     * 修改或者重置密码
     *
     * @param [findByUsername] 用户信息
     * @param [newPassword] 新密码
     * @return [ResultModel]返回结果
     */
    private fun doChangeOrResetPassword(findByUsername: User, newPassword: String): ResultModel {
        val userSalt = findByUsername.salt
        val encryptPassword = PasswordUtil.encryptPassword(userSalt, newPassword)
        val userInput = UserInput(
            findByUsername.userId,
            findByUsername.username,
            encryptPassword,
            findByUsername.salt,
            findByUsername.question,
            findByUsername.answer,
            findByUsername.createTime,
            findByUsername.updateTime
        )
        val save = userRepository.update(userInput)
        return success(save)
    }

    /**
     * 校验用户token
     *
     * @param [username]用户名
     * @param [token]token
     */
    private fun checkResetPasswordToken(username: String, token: String) {
        val cacheToken = cacheManager[CommonConstant.USER_FORGET_PREFIX + username]
        if (Objects.isNull(cacheToken)) {
            throw BizException(ResponseCode.TOKEN_EXPIRE.code, ResponseCode.TOKEN_EXPIRE.msg)
        }
        if (cacheToken != token) {
            throw BizException(msg = "token错误")
        }
    }

    /**
     * 生成并保存忘记密码-校验完密保密码之后的token
     *
     * @param [username]用户名
     * @return [Any]返回生成的token
     */
    private fun generateAndSaveCheckAnswerToken(username: String): Any {
        val token: String = UUIDUtil.uUID
        cacheManager.put(CommonConstant.USER_FORGET_PREFIX + username, token, CommonConstant.FIVE_MINUTES_LONG)
        return token
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
     * @param [username]用户名
     * @param [password]密码
     * @return [User]用户信息
     */
    private fun checkUsernameAndPassword(username: String, password: String): User {
        val findByUserName = userRepository.findByUsername(username)
        return if (findByUserName == null) {
            throw BizException(msg = "用户名错误")
        } else {
            if (findByUserName.password != PasswordUtil.encryptPassword(findByUserName.salt, password)) {
                throw  BizException(msg = "密码错误")
            } else {
                findByUserName
            }
        }
    }
}