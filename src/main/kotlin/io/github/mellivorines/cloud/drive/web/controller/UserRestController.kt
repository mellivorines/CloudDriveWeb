package io.github.mellivorines.cloud.drive.web.controller

import io.github.mellivorines.cloud.drive.web.dao.input.UserInput
import io.github.mellivorines.cloud.drive.web.model.ResultModel
import io.github.mellivorines.cloud.drive.web.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@RestController
@Validated
@Tag(name = "用户相关")
@RequestMapping("/user")
class UserRestController(private val userService: UserService) {


    /**
     * 用户注册
     * @param [userInput] 用户信息
     * @return [ResultModel] 返回结果
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    fun register(@Validated @RequestBody userInput: UserInput): ResultModel {
        return userService.register(userInput)
    }

    /**
     * 登陆账号
     * @param [username]用户名
     * @param [password]密码
     * @return [ResultModel] 返回结果
     */
    @Operation(summary = "登陆")
    @GetMapping("login")
    fun login(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResultModel {
        return userService.login(username, password)
    }

    /**
     * 登陆账号
     * @param [userId] 用户ID
     * @return [ResultModel]返回结果
     */
    @Operation(summary = "退出")
    @GetMapping("exit")
    fun exit(@RequestParam("userId") userId: String): ResultModel {
        return userService.exit(userId)
    }

    /**
     * 获取用户详情
     *
     * @param [userId]用户ID
     * @return [ResultModel]返回结果
     */
    @Operation(summary = "获取用户详情")
    @GetMapping("info")
    fun info(@RequestParam("userId") userId: String): ResultModel {
        return userService.info(userId)
    }

    /**
     * 忘记密码-校验用户名
     *
     * @param [username] 用户名
     * @return [ResultModel]返回结果
     */
    @Operation(summary = "忘记密码-校验用户名")
    @GetMapping("username/check")
    fun checkUsername(@RequestParam("username") username: String): ResultModel {
        return userService.checkUsername(username)
    }

    /**
     * 忘记密码-校验密保答案
     *
     * @param [username] 用户名
     * @param [question]密保问题
     * @param [answer]密保答案
     * @return [ResultModel] 返回结果
     */
    @Operation(summary = "忘记密码-校验密保答案")
    @GetMapping("answer/check")
    fun checkAnswer(
        @RequestParam("username") username: String,
        @RequestParam("question") question: String,
        @RequestParam("answer") answer: String,
    ): ResultModel {
        return userService.checkAnswer(username, question, answer)
    }

    /**
     * 忘记密码-重置密码
     *
     * @param [username] 用户名
     * @param [newPassword]新密码
     * @param [token]token
     * @return [ResultModel]返回结果
     */
    @Operation(summary = "忘记密码-重置密码")
    @GetMapping("password/reset")
    fun resetPassword(
        @RequestParam("username") username: String,
        @RequestParam("newPassword") newPassword: String,
        @RequestParam("token") token: String,
    ): ResultModel {
        return userService.resetPassword(username, newPassword, token)
    }

    /**
     * 用户修改密码
     *
     * @param [userId]用户ID
     * @param [password]原始密码
     * @param [newPassword] 新密码
     * @return [ResultModel] 返回结果
     */
    @Operation(summary = "用户修改密码")
    @GetMapping("password/change")
    fun changePassword(
        @RequestParam("userId") userId: String,
        @RequestParam("password") password: String,
        @RequestParam("newPassword") newPassword: String,
    ): ResultModel {
        return userService.changePassword(userId, password, newPassword)
    }
}