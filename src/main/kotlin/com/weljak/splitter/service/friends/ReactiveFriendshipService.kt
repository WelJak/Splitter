package com.weljak.splitter.service.friends

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.model.friends.Friendship
import com.weljak.splitter.domain.repository.friends.FriendshipRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveFriendshipService(
    private val friendshipRepository: FriendshipRepository,
): FriendshipService {
    private val log = KotlinLogging.logger {  }
    override fun save(friendship: Friendship): Mono<Friendship> {
        return friendshipRepository.save(friendship)
    }

    override fun addFriend(friendship: Friendship, friend: Friend): Mono<Friendship> {
        val updated = friendship.copy(friendList = friendship.friendList.plus(friend))
        return friendshipRepository.save(updated)
    }

    override fun findByUsername(username: String): Mono<Friendship> {
        return friendshipRepository.findByUsername(username)
    }

    override fun deleteFriendFromFriendship(friendship: Friendship, friend: Friend): Mono<Friendship> {
        val updated = friendship.copy(friendList = friendship.friendList.minus(friend))
        return friendshipRepository.save(updated)
    }
}