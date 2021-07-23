package com.weljak.splitter.service.friends.request

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.model.friends.request.FriendshipRequest
import com.weljak.splitter.domain.repository.friends.request.FriendshipRequestRepository
import com.weljak.splitter.service.friends.FriendshipService
import com.weljak.splitter.service.user.UserAccountValidator
import com.weljak.splitter.service.user.UserService
import com.weljak.splitter.utils.api.response.ConfirmationResponse
import com.weljak.splitter.webapi.controller.friends.FriendshipRequestCreationForm
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class ReactiveFriendshipRequestService(
    private val friendshipRequestRepository: FriendshipRequestRepository,
    private val friendshipService: FriendshipService,
    private val userService: UserService,
    private val userAccountValidator: UserAccountValidator
): FriendshipRequestService {
    override fun createFriendshipRequest(username: String, friendshipRequestCreationForm: FriendshipRequestCreationForm): Mono<FriendshipRequest> {
        return userAccountValidator
            .validateFriendRequestForm(username, friendshipRequestCreationForm)
            .flatMap {
                val request = FriendshipRequest(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    username,
                    Friend(
                        it.potentialFriendUsername,
                        it.potentialFriendEmail
                    )
                )
                friendshipRequestRepository.saveFriendshipRequest(request)
            }
            .map { it }
    }

    override fun confirmFriendshipRequest(currentUser:String, requestId: String, confirmationId: String): Mono<ConfirmationResponse> {
        return friendshipRequestRepository
            .findById(requestId)
            .filter { it.potentialFriend.username == currentUser }
            .filter { it.confirmationId == confirmationId }
            .doOnNext { friendshipRequestRepository.deleteFriendshipRequest(it).thenReturn(it) }
            .zipWhen {friendshipService.findByUsername(it.username)}
            .doOnNext { friendshipService.addFriend(it.t2, it.t1.potentialFriend).thenReturn(it) }
            .zipWhen({userService.getByUsername(it.t1.username)}, {friendshipReq, newFriend -> Pair(friendshipReq.t1, newFriend)})
            .zipWhen({friendshipService.findByUsername(it.first.potentialFriend.username)}, {newFriend, newFriendship -> Pair(newFriend.second, newFriendship)})
            .flatMap { friendshipService.addFriend(it.second, Friend(it.first.username, it.first.email)) }
            .map { ConfirmationResponse(true) }
            .switchIfEmpty(Mono.error(RequestDoesNotExistException()))
    }

    override fun deleteFriendshipRequest(requestId: String): Mono<Void> {
        return friendshipRequestRepository
            .findById(requestId)
            .flatMap { friendshipRequestRepository.deleteFriendshipRequest(it) }
    }
}