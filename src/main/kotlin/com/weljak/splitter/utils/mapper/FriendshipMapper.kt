package com.weljak.splitter.utils.mapper

import com.weljak.splitter.domain.model.friends.Friendship
import com.weljak.splitter.domain.model.friends.FriendshipDocument
import com.weljak.splitter.domain.model.friends.request.FriendshipRequest
import com.weljak.splitter.domain.model.friends.request.FriendshipRequestDocument
import com.weljak.splitter.utils.mapper.config.GlobalMapperConfig
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.springframework.stereotype.Component

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, config = GlobalMapperConfig::class)
abstract class FriendshipMapper {
    abstract fun toFriendShip(friendshipDocument: FriendshipDocument): Friendship
    abstract fun toFriendShipDocument(friendship: Friendship): FriendshipDocument
    abstract fun toFriendShipRequestDocument(friendshipRequest: FriendshipRequest): FriendshipRequestDocument
    abstract fun toFriendShipRequest(friendshipRequestDocument: FriendshipRequestDocument): FriendshipRequest
    abstract fun toFriendShipRequests(friendshipRequestDocuments: List<FriendshipRequestDocument>): List<FriendshipRequest>
}