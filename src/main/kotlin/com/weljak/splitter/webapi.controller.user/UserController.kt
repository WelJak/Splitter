package com.weljak.splitter.webapi.controller.user

import com.weljak.splitter.security.Pbkdf2Encoder
import com.weljak.splitter.security.JwtUtils
import com.weljak.splitter.service.user.UserService
import com.weljak.splitter.utils.Endpoints
import com.weljak.splitter.utils.api.response.SplitterResponse
import com.weljak.splitter.utils.api.response.SplitterResponseUtils
import com.weljak.splitter.utils.mapper.UserMapper
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class UserController (
    private val userService: UserService,
    private val jwtUtils: JwtUtils,
    private val pbkdf2Encoder: Pbkdf2Encoder,
    private val userMapper: UserMapper
    ) {
    private val log = KotlinLogging.logger {}

    @GetMapping(Endpoints.USER_DETAILS_ENDPOINT)
    fun getUser(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable username: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        log.info("Fetching user data with username: $username for user: $currentUser")
        return Mono.just(username)
            .flatMap { userService.getByUsername(it) }
            .map { SplitterResponseUtils.success(serverHttpRequest, userMapper.toUserDto(it), "User data", HttpStatus.OK) }
            .defaultIfEmpty(SplitterResponseUtils.empty(serverHttpRequest, null, "User not found", HttpStatus.OK))
    }

    @PostMapping(Endpoints.USER_REGISTER_ENDPOINT)
    fun createUser(
        serverHttpRequest: ServerHttpRequest, @RequestBody userCreationForm: UserCreationForm
    ): Mono<ResponseEntity<SplitterResponse>> {
        log.info("Creating new user with username: ${userCreationForm.username} and email: ${userCreationForm.email}")
        return Mono.just(userCreationForm)
            .flatMap { userService.save(it) }
            .map { SplitterResponseUtils.success(serverHttpRequest, userMapper.toUserDto(it), "User created", HttpStatus.CREATED) }
    }

    @PostMapping(Endpoints.USER_LOGIN_ENDPOINT)
    fun authenticate(serverHttpRequest: ServerHttpRequest, @RequestBody credentials: Credentials): Mono<ResponseEntity<SplitterResponse>> {
        return Mono.just(credentials)
            .flatMap { userService.getByUsername(credentials.username) }
            .filter { pbkdf2Encoder.encode(credentials.password) == it.passwordHash }
            .map {
                SplitterResponseUtils.success(
                    serverHttpRequest,
                    AuthenticationResponse(jwtUtils.generateToken(userMapper.toCurrentUser(it))),
                    "Authentication token acquired",
                    HttpStatus.OK)
            }
            .switchIfEmpty (Mono.just(ResponseEntity(HttpStatus.UNAUTHORIZED)))
    }
}