package com.weljak.splitter.service.friends.request

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.model.friends.request.FriendshipRequest
import com.weljak.splitter.utils.api.response.ConfirmationResponse
import reactor.core.publisher.Mono

interface FriendshipRequestService {
    fun createFriendshipRequest(username: String, potentialFriend: Friend): Mono<FriendshipRequest>
    fun confirmFriendshipRequest(requestId: String, confirmationId: String): Mono<ConfirmationResponse>
    fun deleteFriendshipRequest(requestId: String): Mono<Void>
}