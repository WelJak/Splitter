package com.weljak.splitter.webapi.controller.friends

import com.weljak.splitter.service.friends.FriendshipService
import com.weljak.splitter.utils.Endpoints
import com.weljak.splitter.utils.api.response.SplitterResponse
import com.weljak.splitter.utils.api.response.SplitterResponseUtils
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class FriendsController(
    private val friendshipService: FriendshipService
) {
    private val log = KotlinLogging.logger {  }
    @GetMapping(Endpoints.FRIENDS_ENDPOINT)
    fun getCurrentUserFriendList(serverHttpRequest: ServerHttpRequest, @AuthenticationPrincipal currentUser: String):Mono<ResponseEntity<SplitterResponse>> {
        log.info("Fetching friend list for user: $currentUser")
        return Mono.just(currentUser)
            .flatMap { friendshipService.findByUsername(it) }
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Friend list fetched", HttpStatus.OK) }
    }


}