package com.weljak.splitter.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct

@Component
class JwtUtils {
    @Value("\${springbootwebfluxjjwt.jjwt.secret}")
    private lateinit var secret: String

    @Value("\${springbootwebfluxjjwt.jjwt.expiration}")
    private var expirationTime: Long = 0

    private lateinit var key: Key

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(secret.toByteArray(Charset.forName("UTF-8")))
    }

    fun getClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }

    fun getUsernameFromToken(token: String): String {
        return getClaims(token).subject
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaims(token).expiration
    }

    private fun isTokenExpired(token: String): Boolean {
        return getClaims(token).expiration.before(Date())
    }

    fun generateToken(currentUser: CurrentUser): String {
        val claims = mutableMapOf<String, Any>()
        claims["role"] = currentUser.getRole().toString()
        val createdAt = Date()
        val expirationDate = Date(createdAt.time + expirationTime * 1000)

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(currentUser.username)
            .setIssuedAt(createdAt)
            .setExpiration(expirationDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }
}