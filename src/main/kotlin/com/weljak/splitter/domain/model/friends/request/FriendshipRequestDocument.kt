package com.weljak.splitter.domain.model.friends.request

import com.weljak.splitter.domain.model.friends.Friend
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "friends_requests")
data class FriendshipRequestDocument (
    @Id
    val id: String,
    val confirmationId:String,
    val username: String,
    val potentialFriend: Friend
)