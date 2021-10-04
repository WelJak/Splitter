package com.weljak.splitter.service.group

import com.weljak.splitter.domain.model.group.CreateGroupForm
import com.weljak.splitter.domain.model.group.Group
import com.weljak.splitter.domain.repository.group.GroupRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveGroupService(private val groupRepository: GroupRepository): GroupService {
    override fun createGroup(createGroupForm: CreateGroupForm, createdBy: String): Mono<Group> {
        return groupRepository.createGroup(createGroupForm, createdBy)
    }

    override fun deleteById(id: String): Mono<Void> {
        return groupRepository.deleteById(id)
    }

    override fun findById(id: String): Mono<Group> {
        return groupRepository.findById(id)
    }

    override fun findByGroupName(groupName: String): Mono<List<Group>> {
        return groupRepository.findByGroupName(groupName)
    }

    override fun addMembers(groupId: String, toAdd: List<String>): Mono<Group> {
        return groupRepository.addMembers(groupId, toAdd)
    }

    override fun removeMembers(groupId: String, toDelete: List<String>): Mono<Void> {
        return groupRepository.removeMembers(groupId, toDelete)
    }

    override fun getCurrentUserGroups(currentUser: String): Mono<List<Group>> {
        return groupRepository.getCurrentUserGroups(currentUser)
    }
}