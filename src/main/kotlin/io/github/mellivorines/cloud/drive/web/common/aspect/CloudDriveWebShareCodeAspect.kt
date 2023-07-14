package io.github.mellivorines.cloud.drive.web.common.aspect

import io.github.mellivorines.cloud.drive.web.constants.CommonConstant
import io.github.mellivorines.cloud.drive.web.enum.ResponseCode
import io.github.mellivorines.cloud.drive.web.model.fail
import io.github.mellivorines.cloud.drive.web.utils.JwtUtil
import io.github.mellivorines.cloud.drive.web.utils.ShareIdUtil
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.lang3.StringUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*


/**
 * @Description:请求分享码验证
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/7/3
 */
@Aspect
@Component
class CloudDriveWebShareCodeAspect {

    /**
     * 切点
     */
    @Pointcut(value = "@annotation(io.github.mellivorines.cloud.drive.web.common.annotation.NeedShareCode)")
    fun shareCodeAuth() {
    }

    @Around("shareCodeAuth()")
    @Throws(Throwable::class)
    fun loginAuth(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val servletRequestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        val request: HttpServletRequest = servletRequestAttributes!!.request
        val uri: String = request.getRequestURI()
        log.debug("成功拦截到请求,uri为:{}", uri)
        if (!checkAndSaveShareId(request)) {
            log.warn("成功拦截到请求,uri为:{}, 检测到用户分享码失效,将跳转至分享码输入页面", uri)
            return fail(ResponseCode.CODE_506.msg)
        }
        log.debug("成功拦截到请求,uri为:{}, 请求通过", uri)
        return proceedingJoinPoint.proceed()
    }

    /**
     * 校验分享码token是否有效
     *
     * @param request
     * @return
     */
    private fun checkAndSaveShareId(request: HttpServletRequest): Boolean {
        var token: String = request.getHeader(SHARE_TOKEN_KEY)
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(SHARE_AUTHENTICATION_PARAM_NAME)
        }
        if (StringUtils.isBlank(token)) {
            return false
        }
        val shareId: Any = JwtUtil.analyzeToken(token, CommonConstant.SHARE_ID)
        if (Objects.isNull(shareId)) {
            return false
        }
        saveShareId(shareId)
        return true
    }

    /**
     * 保存分享id到对应的线程上
     *
     * @param shareId
     */
    private fun saveShareId(shareId: Any) {
        ShareIdUtil.set(java.lang.Long.valueOf(shareId.toString()))
    }

    companion object {
        private val log = LoggerFactory.getLogger(CloudDriveWebShareCodeAspect::class.java)

        /**
         * 分享码token的key
         */
        private const val SHARE_TOKEN_KEY = "Share-Token"

        /**
         * 分享认证参数名称
         */
        private const val SHARE_AUTHENTICATION_PARAM_NAME = "shareToken"
    }
}
