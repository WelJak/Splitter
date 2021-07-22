package com.weljak.splitter.domain.repository.friends

import com.weljak.splitter.domain.model.friends.Friendship
import com.weljak.splitter.utils.mapper.FriendshipMapper
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MongoFriendshipRepository(
    private val mongoFriendshipDocumentRepository: MongoFriendshipDocumentRepository,
    private val friendshipMapper: FriendshipMapper
    ): FriendshipRepository {
    override fun save(friendship: Friendship): Mono<Friendship> {
        return mongoFriendshipDocumentRepository.save(friendshipMapper.toFriendShipDocument(friendship)).map { friendship }
    }

    override fun findByUsername(username: String): Mono<Friendship> {
        return mongoFriendshipDocumentRepository.findByUsername(username).map { friendshipMapper.toFriendShip(it) }
    }
}