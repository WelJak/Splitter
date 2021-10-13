package com.weljak.splitter.domain.repository.group

import com.weljak.splitter.domain.model.group.Group
import com.weljak.splitter.domain.model.group.GroupDocument
import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import reactor.core.publisher.Mono

interface GroupRepository {
    fun createGroup(createGroupForm: CreateGroupForm, createdBy: String): Mono<Group>
    fun deleteById(id: String): Mono<Void>
    fun findById(id: String): Mono<Group>
    fun findByGroupName(groupName: String): Mono<List<Group>>
    fun addMembers(groupDocument: GroupDocument, toAdd: List<String>): Mono<Group>
    fun removeMembers(groupDocument: GroupDocument, toDelete: List<String>): Mono<Group>
    fun getCurrentUserGroups(currentUser: String): Mono<List<Group>>
}