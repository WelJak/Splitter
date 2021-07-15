package com.weljak.splitter.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class AuthManager (private val jwtUtils: JwtUtils): ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()
        val username = jwtUtils.getUsernameFromToken(authToken)
        return Mono.just(jwtUtils.validateToken(authToken))
            .filter{it}
            .switchIfEmpty(Mono.empty())
            .map {
                val claims = jwtUtils.getClaims(authToken)
                val role = claims.get("role", String::class.java)
                UsernamePasswordAuthenticationToken(
                    username,
                    authToken,
                    Collections.singletonList(SimpleGrantedAuthority(role))
                )
            }
    }
}