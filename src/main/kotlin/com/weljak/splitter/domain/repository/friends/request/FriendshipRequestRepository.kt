package com.weljak.splitter.domain.repository.friends.request

import com.weljak.splitter.domain.model.friends.request.FriendshipRequest
import reactor.core.publisher.Mono

interface FriendshipRequestRepository {
    fun findByConfirmationId(confirmationId: String): Mono<FriendshipRequest>
    fun findById(confirmationId: String): Mono<FriendshipRequest>
    fun saveFriendshipRequest(friendshipRequest: FriendshipRequest): Mono<FriendshipRequest>
    fun deleteFriendshipRequest(friendshipRequest: FriendshipRequest): Mono<Void>
}