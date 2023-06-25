//package io.github.mellivorines.clouddriveweb.config
//
//import io.jsonwebtoken.Claims
//import io.jsonwebtoken.Jwts
//import io.jsonwebtoken.SignatureAlgorithm
//import io.lettuce.core.dynamic.annotation.Value
//import org.springframework.boot.context.properties.ConfigurationProperties
//import org.springframework.stereotype.Component
//import java.util.*
//
//
///**
// * @Description:
// *
// * @author lilinxi
// * @version 1.0.0
// * @since 2023/6/24
// */
//@Component
//@ConfigurationProperties(prefix = "CloudDrive.jwt")
//data class JwtTool(
//       @Value() val expire: Long,
//       @Value() val secret: String,
//       @Value() val header: String
//) {
//    // 生成JWT
//    fun generateToken(username: String?): String? {
//        val nowDate = Date()
//        val expireDate = Date(nowDate.getTime() + 1000 * expire)
//        return Jwts.builder()
//                .setHeaderParam("typ", "JWT")
//                .setSubject(username)
//                .setIssuedAt(nowDate)
//                .setExpiration(expireDate) // 7天过期
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact()
//    }
//
//    // 解析JWT
//    fun getClaimsByToken(jwt: String?): Claims? {
//        return try {
//            Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(jwt)
//                    .body
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    // 判断JWT是否过期
//    fun isTokenExpired(claims: Claims): Boolean {
//        return claims.expiration.before(Date())
//    }
//}
