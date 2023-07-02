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
     *
     * @param registerPO
     * @return
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    fun register(@Validated @RequestBody userInput: UserInput): ResultModel {
        return userService.register(userInput)
    }

    /**
     * 登陆账号
     */
    @Operation(summary = "登陆")
    @GetMapping("login")
    fun login(@RequestParam("userName") userName: String,
              @RequestParam("password") password: String): ResultModel {
        return userService.login(userName, password)
    }
}