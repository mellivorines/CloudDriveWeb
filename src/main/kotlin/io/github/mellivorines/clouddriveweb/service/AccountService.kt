package io.github.mellivorines.clouddriveweb.service

import io.github.mellivorines.clouddriveweb.dao.entity.User
import io.github.mellivorines.clouddriveweb.dao.input.UserInput
import io.github.mellivorines.clouddriveweb.dao.repository.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Service
import org.springframework.web.bind.support.SessionStatus
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/23
 */
@Service
class AccountService(private val userRepository: UserRepository) {
    /**
     * 注册账号
     * @param [user] 用户信息
     * @return [User] 注册完成的用户信息
     */
    fun register(user: UserInput): Any? {
        //1.检查用户名是否存在
        val findByUserName = userRepository.findByUserName(user.userName)
        return if (findByUserName != null){
            "用户名已经存在！"
        }else {
            //2.构建完整的用户信息
            user.lastLoginTime = LocalDateTime.now(Clock.system(ZoneId.systemDefault()))
            user.joinTime = LocalDateTime.now(Clock.system(ZoneId.systemDefault()))
            user.status = 1
            var toEntity = user.toEntity()
            userRepository.save(toEntity)
        }
    }


    /**
     * 用户登陆
     * @param [userName] 用户名
     * @param [password] 密码
     * @return [User?] 返回用户信息
     */
    fun login(userName: String, password: String): User? {
        return userRepository.findByUserNameAndPassword(userName, password)
    }

    fun logout(session: HttpSession, sessionStatus: SessionStatus): Any? {
        session.invalidate()
        sessionStatus.setComplete()
        return "退出成功！"
    }
}