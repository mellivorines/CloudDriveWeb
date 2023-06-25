//package io.github.mellivorines.clouddriveweb.utils
//
//import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
//import io.jsonwebtoken.Claims
//import io.jsonwebtoken.Jwts
//import io.jsonwebtoken.SignatureAlgorithm
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
//class JwtUtil {
//
//    private val SECRET_KEY = "your_secret_key"
//
//    fun generateToken(username: String?): String? {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(Date())
//                .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact()
//    }
//
//    fun parseToken(token: String?): Claims? {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody()
//    }
//
//}