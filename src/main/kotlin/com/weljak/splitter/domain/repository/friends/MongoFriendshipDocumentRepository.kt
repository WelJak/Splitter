package com.weljak.splitter.domain.repository.friends

import com.weljak.splitter.domain.model.friends.FriendshipDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface MongoFriendshipDocumentRepository: ReactiveMongoRepository<FriendshipDocument, String> {
    fun findByUsername(username: String): Mono<FriendshipDocument>
}