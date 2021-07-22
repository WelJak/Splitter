package com.weljak.splitter.domain.model.friends

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "friends")
data class FriendshipDocument(
    @Id
    val id: String,
    @Indexed(unique = true)
    val username: String,
    val friendList: List<Friend> = emptyList()
)
