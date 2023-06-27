package io.github.mellivorines.clouddriveweb.controller

import io.github.mellivorines.clouddriveweb.dao.input.UserInput
import io.github.mellivorines.clouddriveweb.model.ResultModel
import io.github.mellivorines.clouddriveweb.model.fail
import io.github.mellivorines.clouddriveweb.model.success
import io.github.mellivorines.clouddriveweb.service.AccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpSession
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.SessionStatus


/**
 * @Description:账号相关Controller
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/10
 */
@Tag(name = "账号管理")
@RestController
@RequestMapping("/account")
@Transactional
class AccountController(private val accountService: AccountService) {

    /**
     * 注册账号
     */
    @Operation(summary = "注册")
    @PostMapping("register")
    fun register(@RequestBody user: UserInput): ResultModel {
        return success(accountService.register(user))
    }

    /**
     * 登陆账号
     */
    @Operation(summary = "登陆")
    @GetMapping("login")
    fun login(
        @RequestParam("userName") userName: String,
        @RequestParam("password") password: String
    ): ResultModel {
        return success(accountService.login(userName, password))
    }

    /**
     * 退出账号
     */
    @Operation(summary = "退出")
    @GetMapping("logout")
    @ResponseBody
    fun logout(session: HttpSession, sessionStatus: SessionStatus): ResultModel {
        return try {
            success(accountService.logout(session, sessionStatus))
        } catch (e: Exception) {
            e.printStackTrace()
            fail(e.message)
        }
    }

}