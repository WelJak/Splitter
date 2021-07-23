package com.weljak.splitter.domain.repository.user

import com.weljak.splitter.domain.model.user.UserDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface MongoUserDocumentRepository: ReactiveMongoRepository<UserDocument, String> {
    fun findByUsername(username: String): Mono<UserDocument>
    fun existsByUsername(username: String): Mono<Boolean>
    fun existsByEmail(email: String): Mono<Boolean>
    fun existsByUsernameAndAndEmail(username: String, email: String): Mono<Boolean>
}