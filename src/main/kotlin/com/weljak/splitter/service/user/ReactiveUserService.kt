package com.weljak.splitter.service.user

import com.weljak.splitter.domain.model.friends.Friendship
import com.weljak.splitter.domain.model.user.User
import com.weljak.splitter.domain.repository.user.UserRepository
import com.weljak.splitter.service.friends.FriendshipService
import com.weljak.splitter.utils.mapper.UserMapper
import com.weljak.splitter.webapi.controller.user.UserCreationForm
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class ReactiveUserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val userAccountValidator: UserAccountValidator,
    private val friendshipService: FriendshipService
) : UserService {
    override fun getByUsername(username: String): Mono<User> {
        return userRepository.findByUsername(username)
    }

    override fun save(userCreationForm: UserCreationForm): Mono<User> {
        return Mono.just(userCreationForm)
            .flatMap { userAccountValidator.validateRegistrationForm(it) }
            .flatMap { friendshipService.save(Friendship(UUID.randomUUID().toString(), it.username)).thenReturn(it) }
            .flatMap { userRepository.save(userMapper.toUser(it)) }
            .map { it }
    }
}