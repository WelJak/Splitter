package com.weljak.splitter.service.friends

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.model.friends.Friendship
import com.weljak.splitter.domain.model.friends.FriendshipDocument
import com.weljak.splitter.domain.repository.friends.FriendshipRepository
import mu.KotlinLogging
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveFriendshipService(
    private val friendshipRepository: FriendshipRepository,
    private val reactiveMongoTemplate: ReactiveMongoTemplate
): FriendshipService {
    private val log = KotlinLogging.logger {  }
    override fun save(friendship: Friendship): Mono<Friendship> {
        return friendshipRepository.save(friendship)
    }

    override fun addFriend(friendship: Friendship, friend: Friend): Mono<Friendship> {
        val updated = friendship.copy(friendList = friendship.friendList.plus(friend))
        return update(updated)
    }

    override fun findByUsername(username: String): Mono<Friendship> {
        return friendshipRepository.findByUsername(username)
    }

    override fun deleteFriendFromFriendship(friendship: Friendship, friend: Friend): Mono<Friendship> {
        val updated = friendship.copy(friendList = friendship.friendList.minus(friend))
        return update(updated)
    }

    private fun update(friendship: Friendship): Mono<Friendship> {
        val update = Update()
        update.set("friendList", friendship.friendList)
        return reactiveMongoTemplate.updateFirst(query(where("username").`is`(friendship.username)), update, FriendshipDocument::class.java)
            .map { friendship }
    }
}