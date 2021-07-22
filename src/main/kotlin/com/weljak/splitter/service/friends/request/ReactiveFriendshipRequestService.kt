package com.weljak.splitter.service.friends.request

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.model.friends.request.FriendshipRequest
import com.weljak.splitter.domain.repository.friends.request.FriendshipRequestRepository
import com.weljak.splitter.service.friends.FriendshipService
import com.weljak.splitter.utils.api.response.ConfirmationResponse
import reactor.core.publisher.Mono
import java.util.*

class ReactiveFriendshipRequestService(
    private val friendshipRequestRepository: FriendshipRequestRepository,
    private val friendshipService: FriendshipService
): FriendshipRequestService {
    override fun createFriendshipRequest(username: String, potentialFriend: Friend): Mono<FriendshipRequest> {
        val request = FriendshipRequest(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            username,
            potentialFriend
        )
        return friendshipRequestRepository.saveFriendshipRequest(request)
    }

    override fun confirmFriendshipRequest(requestId: String, confirmationId: String): Mono<ConfirmationResponse> {
        return friendshipRequestRepository
            .findById(requestId)
            .filter { it.confirmationId == confirmationId }
            .doOnNext { friendshipRequestRepository.deleteFriendshipRequest(it) }
            .zipWhen {friendshipService.findByUsername(it.username)}
            .flatMap { friendshipService.addFriend(it.t2, it.t1.potentialFriend) }
            .map { ConfirmationResponse(true) }
            .switchIfEmpty(Mono.error(RequestDoesNotExistException()))
    }

    override fun deleteFriendshipRequest(requestId: String): Mono<Void> {
        return friendshipRequestRepository
            .findById(requestId)
            .flatMap { friendshipRequestRepository.deleteFriendshipRequest(it) }
    }
}