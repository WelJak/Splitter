package com.weljak.splitter.domain.repository.friends.request

import com.weljak.splitter.domain.model.friends.request.FriendshipRequest
import com.weljak.splitter.domain.model.friends.request.FriendshipRequestDocument
import com.weljak.splitter.utils.mapper.FriendshipMapper
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MongoFriendshipRequestRepository(
    private val mongoFriendshipRequestDocumentRepository: MongoFriendshipRequestDocumentRepository,
    private val friendshipMapper: FriendshipMapper
    ): FriendshipRequestRepository{
    override fun findByConfirmationId(confirmationId: String): Mono<FriendshipRequest> {
        return mongoFriendshipRequestDocumentRepository
            .findByConfirmationId(confirmationId)
            .map { friendshipMapper.toFriendShipRequest(it) }
    }

    override fun findById(confirmationId: String): Mono<FriendshipRequest> {
        return mongoFriendshipRequestDocumentRepository
            .findById(confirmationId)
            .map { friendshipMapper.toFriendShipRequest(it) }
    }

    override fun saveFriendshipRequest(friendshipRequest: FriendshipRequest): Mono<FriendshipRequest> {
        return mongoFriendshipRequestDocumentRepository
            .save(friendshipMapper.toFriendShipRequestDocument(friendshipRequest))
            .map { friendshipRequest }
    }

    override fun deleteFriendshipRequest(friendshipRequest: FriendshipRequest): Mono<Void> {
        return mongoFriendshipRequestDocumentRepository.deleteById(friendshipRequest.id)
    }

    override fun findAllByUsername(username: String): Mono<List<FriendshipRequest>> {
        return mongoFriendshipRequestDocumentRepository
            .findAllByUsername(username)
            .collectList()
            .map { friendshipMapper.toFriendShipRequests(it) }
    }

    override fun findAllByPotentialFriendUsername(potentialFriendUsername: String): Mono<List<FriendshipRequest>> {
        return mongoFriendshipRequestDocumentRepository
            .findAll()
            .filter { request -> request.potentialFriend.username == potentialFriendUsername }
            .collectList()
            .map { friendshipMapper.toFriendShipRequests(it) }
    }
}