package com.weljak.splitter.service.group

import com.weljak.splitter.domain.model.group.Group
import com.weljak.splitter.domain.model.group.GroupDocument
import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import reactor.core.publisher.Mono

interface GroupService {
    fun createGroup(createGroupForm: CreateGroupForm, createdBy: String): Mono<Group>
    fun deleteById(id: String, currentUser: String): Mono<Void>
    fun findById(id: String): Mono<Group>
    fun findByGroupName(groupName: String): Mono<List<Group>>
    fun addMembers(groupDocument: GroupDocument, toAdd: List<String>, currentUser: String): Mono<Group>
    fun removeMembers(groupDocument: GroupDocument, toDelete: List<String>, currentUser: String): Mono<Group>
    fun getCurrentUserGroups(currentUser: String): Mono<List<Group>>
    fun leaveGroup(currentUser: String, groupDocument: GroupDocument): Mono<Void>
}