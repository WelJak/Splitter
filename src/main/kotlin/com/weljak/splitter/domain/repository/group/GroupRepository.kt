package com.weljak.splitter.domain.repository.group

import com.weljak.splitter.domain.model.group.CreateGroupForm
import com.weljak.splitter.domain.model.group.Group
import reactor.core.publisher.Mono

interface GroupRepository {
    fun createGroup(createGroupForm: CreateGroupForm, createdBy: String): Mono<Group>
    fun deleteById(id: String): Mono<Void>
    fun findById(id: String): Mono<Group>
    fun findByGroupName(groupName: String): Mono<List<Group>>
    fun addMembers(groupId: String, toAdd: List<String>): Mono<Group>
    fun removeMembers(groupId: String, toDelete: List<String>): Mono<Void>
    fun getCurrentUserGroups(currentUser: String): Mono<List<Group>>
}