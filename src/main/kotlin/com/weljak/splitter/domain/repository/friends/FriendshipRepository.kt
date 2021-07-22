package com.weljak.splitter.domain.repository.friends

import com.weljak.splitter.domain.model.friends.Friendship
import reactor.core.publisher.Mono

interface FriendshipRepository {
    fun save(friendship: Friendship): Mono<Friendship>
    fun findByUsername(username: String): Mono<Friendship>
}