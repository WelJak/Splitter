package com.weljak.splitter.service.group

import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import com.weljak.splitter.domain.model.group.Group
import com.weljak.splitter.domain.repository.group.GroupRepository
import kotlin.collections.ArrayList
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveGroupService(
    private val groupRepository: GroupRepository,
    private val groupValidator: GroupValidator
    ): GroupService {
    private val log = KotlinLogging.logger {  }
    override fun createGroup(createGroupForm: CreateGroupForm, createdBy: String): Mono<Group> {
        createGroupForm.members?.add(createdBy)
        return groupValidator.validateGroupCreationForm(createGroupForm, createdBy)
            .flatMap { groupRepository.createGroup(it, createdBy) }
    }

    override fun deleteById(id: String, currentUser: String): Mono<Void> {
        return groupRepository.findById(id)
            .filter { it.createdBy == currentUser }
            .flatMap { groupRepository.deleteById(id) }
            .switchIfEmpty(Mono.error(UserNotOwnerOfGroupException("Can't delete group with id: $id - $currentUser is not owner of group")))
    }

    override fun findById(id: String): Mono<Group> {
        return groupRepository.findById(id)
    }

    override fun findByGroupName(groupName: String): Mono<List<Group>> {
        return groupRepository.findByGroupName(groupName)
    }

    override fun addMembers(groupId: String, toAdd: List<String>, currentUser: String): Mono<Group> {
        return groupRepository.findById(groupId)
            .filter { it.createdBy == currentUser }
            .map{ trimRedundantMembers(it, ArrayList(toAdd)) }
            .flatMap { groupRepository.addMembers(groupId, toAdd) }
            .switchIfEmpty(Mono.error(UserNotOwnerOfGroupException("Can't add users to group with id: $groupId - $currentUser is not owner of the group")))
    }

    override fun removeMembers(groupId: String, toDelete: List<String>, currentUser: String): Mono<Group> {
//        return groupRepository.findById(groupId)
////            .filter { it.createdBy == currentUser && !toDelete.contains(currentUser) }
////            .doOnNext { if (toDelete.contains(currentUser)) {
////                throw UserNotOwnerOfGroupException("Can't remove users from group with id: $groupId - $currentUser is not owner of the group or was pointed to be removed from group as a owner")
////            } }
//            .flatMap {
//                groupRepository.removeMembers(groupId, toDelete)
//            }
//            .then()
//            .switchIfEmpty(Mono.error(UserNotOwnerOfGroupException("Can't remove users from group with id: $groupId - $currentUser is not owner of the group or was pointed to be removed from group as a owner")) )
        return groupRepository.removeMembers(groupId, toDelete)
    }

    override fun getCurrentUserGroups(currentUser: String): Mono<List<Group>> {
        return groupRepository.getCurrentUserGroups(currentUser)
    }

    override fun leaveGroup(currentUser: String, groupId: String): Mono<Void> {
        return groupRepository.findById(groupId)
            .filter { it.createdBy == currentUser }
            .flatMap { groupRepository.deleteById(groupId) }
//            .switchIfEmpty(groupRepository.removeMembers(groupId, Collections.singletonList(currentUser)))
            .then()
    }

    private fun trimRedundantMembers(group: Group, toAdd: ArrayList<String>): List<String> {
        toAdd.removeAll(group.members!!)
        return toAdd
    }
}