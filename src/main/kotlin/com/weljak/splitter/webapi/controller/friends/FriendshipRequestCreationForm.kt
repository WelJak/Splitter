package com.weljak.splitter.webapi.controller.friends

import com.weljak.splitter.domain.model.friends.Friend

data class FriendshipRequestCreationForm(
    val potentialFriendUsername: String,
    val potentialFriendEmail: String

)
