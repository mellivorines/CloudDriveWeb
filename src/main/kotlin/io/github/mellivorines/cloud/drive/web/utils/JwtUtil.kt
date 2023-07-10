package io.github.mellivorines.cloud.drive.web.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*


/**
 * @Description:Jwt工具类
 *
 * @author lilinxi
 * @version 1.0.0
 * @since 2023/6/29
 */
object JwtUtil {
    private const val TWO_LONG = 2L
    const val EMPTY_STR = ""

    /**
     * 秘钥
     */
    private const val JWT_PRIVATE_KEY = "0123456789"

    /**
     * 刷新时间
     */
    private const val RENEWAL_TIME = "RENEWAL_TIME"

    /**
     * 生成token
     *
     * @param subject
     * @param claimKey
     * @param claimValue
     * @param expire
     * @return
     */
    fun generateToken(
        subject: String,
        claimKey: String,
        claimValue: Any,
        expire: Long
    ): String {
        return Jwts.builder()
            .setSubject(subject)
            .claim(claimKey, claimValue)
            .claim(RENEWAL_TIME, Date(System.currentTimeMillis() + expire / TWO_LONG))
            .setExpiration(Date(System.currentTimeMillis() + expire))
            .signWith(SignatureAlgorithm.HS256, JWT_PRIVATE_KEY)
            .compact()
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    fun analyzeToken(token: String, claimKey: String): Any {
        return {
            val claims = Jwts.parser()
                .setSigningKey(JWT_PRIVATE_KEY)
                .parseClaimsJws(token)
                .body
            claims[claimKey]
        }
    }
}
