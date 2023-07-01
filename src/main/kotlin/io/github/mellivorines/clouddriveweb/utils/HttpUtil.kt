package io.github.mellivorines.clouddriveweb.utils

import jakarta.servlet.http.HttpServletResponse


/**
 * @Description:Http工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object HttpUtil {
    /**
     * 添加跨域的响应头
     *
     * @param response
     */
    fun addCorsResponseHeader(response: HttpServletResponse) {
        response.setHeader(CorsConfigEnum.CORS_ORIGIN.key, CorsConfigEnum.CORS_ORIGIN.value)
        response.setHeader(CorsConfigEnum.CORS_CREDENTIALS.key, CorsConfigEnum.CORS_CREDENTIALS.value)
        response.setHeader(CorsConfigEnum.CORS_METHODS.key, CorsConfigEnum.CORS_METHODS.value)
        response.setHeader(CorsConfigEnum.CORS_MAX_AGE.key, CorsConfigEnum.CORS_MAX_AGE.value)
        response.setHeader(CorsConfigEnum.CORS_HEADERS.key, CorsConfigEnum.CORS_HEADERS.value)
    }

    /**
     * 跨域设置枚举类
     */
    enum class CorsConfigEnum(val key: String, val value: String) {
        /**
         * 允许所有远程访问
         */
        CORS_ORIGIN("Access-Control-Allow-Origin", "*"),

        /**
         * 允许认证
         */
        CORS_CREDENTIALS("Access-Control-Allow-Credentials", "true"),

        /**
         * 允许远程调用的请求类型
         */
        CORS_METHODS("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT"),

        /**
         * 指定本次预检请求的有效期，单位是秒
         */
        CORS_MAX_AGE("Access-Control-Max-Age", "3600"),

        /**
         * 允许所有请求头
         */
        CORS_HEADERS("Access-Control-Allow-Headers", "*")

    }
}
