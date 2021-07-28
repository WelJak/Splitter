package com.weljak.splitter.webapi.controller.friends

import com.weljak.splitter.service.friends.FriendshipService
import com.weljak.splitter.service.friends.request.FriendshipRequestService
import com.weljak.splitter.utils.Endpoints
import com.weljak.splitter.utils.api.response.SplitterResponse
import com.weljak.splitter.utils.api.response.SplitterResponseUtils
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class FriendsController(
    private val friendshipService: FriendshipService,
    private val friendshipRequestService: FriendshipRequestService
) {
    private val log = KotlinLogging.logger {  }

    @GetMapping(Endpoints.FRIENDS_ENDPOINT)
    fun getCurrentUserFriendList(serverHttpRequest: ServerHttpRequest, @AuthenticationPrincipal currentUser: String):Mono<ResponseEntity<SplitterResponse>> {
        log.info("Fetching friend list for user: $currentUser")
        return Mono.just(currentUser)
            .flatMap { friendshipService.findByUsername(it) }
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Friend list fetched", HttpStatus.OK) }
    }

    @PostMapping(Endpoints.CREATE_FRIENDS_REQUEST_ENDPOINT)
    fun createFriendsRequest(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @RequestBody friendshipRequestCreationForm: FriendshipRequestCreationForm
): Mono<ResponseEntity<SplitterResponse>> {
        return Mono.just(friendshipRequestCreationForm)
            .flatMap { friendshipRequestService.createFriendshipRequest(currentUser, it) }
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Friend request created", HttpStatus.CREATED) }
            .onErrorReturn (SplitterResponseUtils.error(serverHttpRequest, null, "Error during creating friend request", HttpStatus.BAD_REQUEST))
    }

    @GetMapping(Endpoints.CONFIRM_FRIENDS_REQUEST_ENDPOINT)
    fun confirmFriendRequest(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable reqId: String,
        @PathVariable confirmationId: String
): Mono<ResponseEntity<SplitterResponse>> {
        return friendshipRequestService
            .confirmFriendshipRequest(currentUser,reqId,confirmationId)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Friend request accepted", HttpStatus.OK) }
    }
}