package com.weljak.splitter.service.friends.request

import com.weljak.splitter.domain.model.friends.request.FriendshipRequest
import com.weljak.splitter.utils.api.response.ConfirmationResponse
import com.weljak.splitter.webapi.controller.friends.FriendshipRequestCreationForm
import reactor.core.publisher.Mono

interface FriendshipRequestService {
    fun createFriendshipRequest(username: String, friendshipRequestCreationForm: FriendshipRequestCreationForm): Mono<FriendshipRequest>
    fun confirmFriendshipRequest(currentUser: String,requestId: String, confirmationId: String): Mono<ConfirmationResponse>
    fun deleteFriendshipRequest(requestId: String): Mono<Void>
    fun findAllFriendRequestsMadeByUser(username: String): Mono<List<FriendshipRequest>>
    fun findAllFriendRequestReceivedByUser(username: String): Mono<List<FriendshipRequest>>
}