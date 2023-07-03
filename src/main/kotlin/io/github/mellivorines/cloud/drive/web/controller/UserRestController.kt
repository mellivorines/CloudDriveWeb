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
@Tag(name = "文件接口")
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
     * @param [userName]用户名
     * @param [password]密码
     * @return [ResultModel] 返回结果
     */
    @Operation(summary = "登陆")
    @GetMapping("login")
    fun login(
        @RequestParam("userName") userName: String,
        @RequestParam("password") password: String
    ): ResultModel {
        return userService.login(userName, password)
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
}