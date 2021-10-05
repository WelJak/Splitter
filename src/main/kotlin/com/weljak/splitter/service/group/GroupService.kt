package com.weljak.splitter.service.group

import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import com.weljak.splitter.domain.model.group.Group
import reactor.core.publisher.Mono

interface GroupService {
    fun createGroup(createGroupForm: CreateGroupForm, createdBy: String): Mono<Group>
    fun deleteById(id: String, currentUser: String): Mono<Void>
    fun findById(id: String): Mono<Group>
    fun findByGroupName(groupName: String): Mono<List<Group>>
    fun addMembers(groupId: String, toAdd: List<String>, currentUser: String): Mono<Group>
    fun removeMembers(groupId: String, toDelete: List<String>, currentUser: String): Mono<Group>
    fun getCurrentUserGroups(currentUser: String): Mono<List<Group>>
    fun leaveGroup(currentUser: String, groupId: String): Mono<Void>
}