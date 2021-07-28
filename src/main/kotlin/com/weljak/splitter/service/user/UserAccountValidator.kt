package com.weljak.splitter.service.user

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.repository.friends.MongoFriendshipDocumentRepository
import com.weljak.splitter.domain.repository.friends.request.MongoFriendshipRequestDocumentRepository
import com.weljak.splitter.domain.repository.user.MongoUserDocumentRepository
import com.weljak.splitter.service.friends.FriendshipAlreadyExistsException
import com.weljak.splitter.service.friends.request.FriendRequestAlreadyExistsException
import com.weljak.splitter.webapi.controller.friends.FriendshipRequestCreationForm
import com.weljak.splitter.webapi.controller.user.UserCreationForm
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserAccountValidator(
    private val mongoUserDocumentRepository: MongoUserDocumentRepository,
    private val mongoFriendshipDocumentRepository: MongoFriendshipDocumentRepository,
    private val mongoFriendshipRequestDocumentRepository: MongoFriendshipRequestDocumentRepository
) {
    fun validateRegistrationForm(userCreationForm: UserCreationForm): Mono<UserCreationForm> {
        return Mono
            .just(userCreationForm)
            .zipWhen({ mongoUserDocumentRepository.existsByUsername(it.username) }, { it, exist -> Pair(it, exist) })
            .doOnNext {
                if (it.second) {
                    throw UserAlreadyExistsException("User with given username already exists.")
                }
            }
            .zipWhen({ mongoUserDocumentRepository.existsByEmail(it.first.email) }, { it, exist -> Pair(it.first, exist) })
            .doOnNext {
                if (it.second) {
                    throw UserAlreadyExistsException("User with given email already exists.")
                }
            }
            .map { it.first }
    }

    fun validateFriendRequestForm(currentUserName: String,friendshipRequestCreationForm: FriendshipRequestCreationForm): Mono<FriendshipRequestCreationForm> {
        return Mono.just(friendshipRequestCreationForm)
            .zipWhen({mongoUserDocumentRepository.existsByUsernameAndAndEmail(
                it.potentialFriendUsername,
                it.potentialFriendEmail
            )}, {it, exists -> Pair(it, exists)})
            .doOnNext { if (!it.second) {
                throw UserDoesNotExistsException("User with given username and email does not exist.")
            } }
            .zipWhen({ requestExistsByUsernameAndPotentialFriend(
                currentUserName, Friend(it.first.potentialFriendUsername, it.first.potentialFriendEmail)
            )}, {it, exists -> Pair(it.first, exists)})
            .doOnNext { if (!it.second) {
                throw FriendRequestAlreadyExistsException("Friend request with given details already exists.")
            } }
            .zipWhen(
                {friendshipExistsByUsernameAndFriend(currentUserName, Friend(it.first.potentialFriendUsername, it.first.potentialFriendEmail))},
                {it, exists -> Pair(it.first, exists)}
            )
            .doOnNext { if (!it.second) {
                throw FriendshipAlreadyExistsException("Friendship between given users already exist.")
            } }
            .map { it.first }
    }

    private fun requestExistsByUsernameAndPotentialFriend(username: String, potentialFriend: Friend): Mono<Boolean> {
        return mongoFriendshipRequestDocumentRepository.findAll()
            .filter { document -> document.username == username && document.potentialFriend == potentialFriend }
            .collectList()
            .map { it.isEmpty() }
    }

    private fun friendshipExistsByUsernameAndFriend(username: String, friend: Friend): Mono<Boolean> {
        return mongoFriendshipDocumentRepository.findAll()
            .filter { document -> document.username == username && document.friendList.contains(friend) }
            .collectList()
            .map { it.isEmpty() }
    }
}