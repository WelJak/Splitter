package com.weljak.splitter.domain.model.friends

data class Friendship (
    val id: String,
    val username: String,
    val friendList: List<Friend> = emptyList()
)