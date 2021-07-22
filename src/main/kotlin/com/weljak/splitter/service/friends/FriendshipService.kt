package com.weljak.splitter.service.friends

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.model.friends.Friendship
import reactor.core.publisher.Mono

interface FriendshipService {
    fun save(friendship: Friendship): Mono<Friendship>
    fun addFriend(friendship: Friendship, friend: Friend): Mono<Friendship>
    fun findByUsername(username: String): Mono<Friendship>
    fun deleteFriendFromFriendship(friendship: Friendship, friend: Friend): Mono<Friendship>
}