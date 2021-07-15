package com.weljak.splitter.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
@Component
class SecurityContextRepository(private val authManager: AuthManager): ServerSecurityContextRepository {


    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        return Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter {it.startsWith("Bearer ")}
            .flatMap { token ->
                val authToken = token.substring(7)
                val auth = UsernamePasswordAuthenticationToken(authToken, authToken)
                authManager.authenticate(auth).map {SecurityContextImpl(it)}
            }
    }
}