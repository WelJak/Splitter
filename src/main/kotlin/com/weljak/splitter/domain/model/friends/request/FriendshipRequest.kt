package com.weljak.splitter.domain.model.friends.request

import com.weljak.splitter.domain.model.friends.Friend

data class FriendshipRequest(
    val id: String,
    val confirmationId:String,
    val username: String,
    val potentialFriend: Friend
)
