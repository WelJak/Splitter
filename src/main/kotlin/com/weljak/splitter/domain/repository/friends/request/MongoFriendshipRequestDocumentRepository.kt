package com.weljak.splitter.domain.repository.friends.request

import com.weljak.splitter.domain.model.friends.request.FriendshipRequestDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MongoFriendshipRequestDocumentRepository: ReactiveMongoRepository<FriendshipRequestDocument, String> {
    fun findByConfirmationId(confirmationId: String): Mono<FriendshipRequestDocument>
    fun findAllByUsername(username: String): Flux<FriendshipRequestDocument>
}