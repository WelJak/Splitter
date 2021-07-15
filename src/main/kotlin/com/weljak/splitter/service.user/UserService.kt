package com.weljak.splitter.service.user

import com.weljak.splitter.domain.model.user.User
import com.weljak.splitter.webapi.controller.user.UserCreationForm
import reactor.core.publisher.Mono

interface UserService {
    fun getByUsername(username: String): Mono<User>
    fun save(userCreationForm: UserCreationForm): Mono<User>
}