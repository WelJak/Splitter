package com.weljak.splitter.domain.repository.user

import com.weljak.splitter.domain.model.user.User
import com.weljak.splitter.security.CurrentUser
import reactor.core.publisher.Mono

interface UserRepository {
    fun save(user: User): Mono<User>
    fun findByUsername(username: String): Mono<User>
    fun getCurrentUserByUsername(username: String): Mono<CurrentUser>
}