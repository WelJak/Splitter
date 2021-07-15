package com.weljak.splitter.domain.repository.user

import com.weljak.splitter.domain.model.user.User
import com.weljak.splitter.security.CurrentUser
import com.weljak.splitter.service.user.UserAlreadyExistsException
import com.weljak.splitter.utils.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MongoUserRepository
    @Autowired
    constructor (
        private val mongoUserDocumentRepository: MongoUserDocumentRepository,
        private val userMapper: UserMapper
        ): UserRepository {
    override fun save(user: User): Mono<User> {
        return mongoUserDocumentRepository.save(userMapper.toUserDocument(user)).map { user }
    }

    override fun findByUsername(username: String): Mono<User> {
        return mongoUserDocumentRepository.findByUsername(username).map { userMapper.toUser(it) }
    }

    override fun getCurrentUserByUsername(username: String): Mono<CurrentUser> {
        return mongoUserDocumentRepository.findByUsername(username).map { userMapper.toCurrentUser(userMapper.toUser(it)) }
    }
}