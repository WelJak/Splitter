package com.weljak.splitter.security

import com.weljak.splitter.utils.Endpoints
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    private val authManager: AuthManager,
    private val securityContextRepository: SecurityContextRepository
    ) {
    @Bean
    fun securityWebFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        return httpSecurity
            .exceptionHandling()
            .authenticationEntryPoint { swe, _ -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.UNAUTHORIZED } }
            .accessDeniedHandler { swe, _ -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN } }
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authenticationManager(authManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange()
            .pathMatchers(Endpoints.USER_LOGIN_ENDPOINT).permitAll()
            .pathMatchers(Endpoints.USER_REGISTER_ENDPOINT).permitAll()
            .pathMatchers(Endpoints.OPENAPI_3_DOCUMENTATION_ENDPOINT).permitAll()
            .pathMatchers(Endpoints.OPENAPI_3_UI_ENDPOINT).permitAll()
            .pathMatchers(Endpoints.WEBJARS_ENDPOINT).permitAll()
            .anyExchange().authenticated()
            .and().build()
    }
}