package com.weljak.splitter.service.user

import com.weljak.splitter.domain.repository.user.MongoUserDocumentRepository
import com.weljak.splitter.webapi.controller.user.UserCreationForm
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserAccountValidator(private val mongoUserDocumentRepository: MongoUserDocumentRepository) {
    fun validate(userCreationForm: UserCreationForm): Mono<UserCreationForm> {
        return Mono
            .just(userCreationForm)
            .zipWhen({ mongoUserDocumentRepository.existsByUsername(it.username) }, { it, exist -> Pair(it, exist) })
            .doOnNext {
                if (it.second) {
                    throw UserAlreadyExistsException("User with given username already exists.")
                }
            }
            .zipWhen({ mongoUserDocumentRepository.existsByEmail(it.first.email) }, { it, exist -> Pair(it.first, exist) })
            .doOnNext {
                if (it.second) {
                    throw UserAlreadyExistsException("User with given email already exists.")
                }
            }
            .map { it.first }
    }
}