package io.github.mellivorines.cloud.drive.web.common.aspect

import io.github.mellivorines.cloud.drive.web.cache.core.CacheManager
import io.github.mellivorines.cloud.drive.web.constants.CommonConstant
import io.github.mellivorines.cloud.drive.web.enum.ResponseCode
import io.github.mellivorines.cloud.drive.web.model.fail
import io.github.mellivorines.cloud.drive.web.utils.JwtUtil
import io.github.mellivorines.cloud.drive.web.utils.UserIdUtil
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.lang3.StringUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*


/**
 * @Description:
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/2
 */
@Aspect
@Component
class CloudDriveWebLoginAspect {
    private val log: Logger = LoggerFactory.getLogger(CloudDriveWebLoginAspect::class.java)

    /**
     * 登录认证参数名称
     */
    private val LOGIN_AUTHENTICATION_PARAM_NAME = "authorization"

    /**
     * 请求头token的key
     */
    private val TOKEN_KEY = "Authorization"

    @Autowired
    private lateinit var cacheManager: CacheManager

    /**
     * 切点
     */
    @Pointcut(value = "@annotation(io.github.mellivorines.cloud.drive.web.common.annotation.NeedLogin)")
    fun loginAuth() {
    }

    @Around("loginAuth()")
    @Throws(Throwable::class)
    fun loginAuth(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val servletRequestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        val request: HttpServletRequest = servletRequestAttributes!!.request
        val uri: String = request.getRequestURI()
        log.debug("成功拦截到请求,uri为:{}", uri)
        if (!checkAndSaveUserId(request)) {
            log.warn("成功拦截到请求,uri为:{}, 检测到用户未登录,将跳转至登录页面", uri)
            return fail(ResponseCode.NEED_LOGIN.msg)
        }
        log.debug("成功拦截到请求,uri为:{}, 请求通过", uri)
        return proceedingJoinPoint.proceed()
    }

    /**
     * 检查并保存登录用户的ID
     * 此处会实现单设备登录功能 所以本套代码未考虑并发
     *
     * @param request
     * @return
     */
    private fun checkAndSaveUserId(request: HttpServletRequest): Boolean {
        var token: String = request.getHeader(TOKEN_KEY)
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(LOGIN_AUTHENTICATION_PARAM_NAME)
        }
        if (StringUtils.isBlank(token)) {
            return false
        }
        val userId: Any = JwtUtil.analyzeToken(token, CommonConstant.LOGIN_USER_ID)
        if (Objects.isNull(userId)) {
            return false
        }
        val redisValue: Any? = cacheManager.get(CommonConstant.USER_LOGIN_PREFIX + userId)
        if (Objects.isNull(redisValue)) {
            return false
        }
        if (redisValue == token) {
            saveUserId(userId)
            return true
        }
        return false
    }

    /**
     * 保存用户ID到对应线程上
     *
     * @param userId
     */
    private fun saveUserId(userId: Any) {
        UserIdUtil.set(java.lang.Long.valueOf(userId.toString()))
    }
}