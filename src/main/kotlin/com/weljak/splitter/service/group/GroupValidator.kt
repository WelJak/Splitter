package com.weljak.splitter.service.group

import com.weljak.splitter.domain.model.friends.Friend
import com.weljak.splitter.domain.repository.friends.FriendshipRepository
import com.weljak.splitter.utils.exception.ValidationException
import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class GroupValidator(private val friendshipRepository: FriendshipRepository) {
    fun validateGroupCreationForm(createGroupForm: CreateGroupForm, currentUser: String): Mono<CreateGroupForm> {
        return friendshipRepository.findByUsername(currentUser)
            .map { validateGroupMembers(createGroupForm.members!!, it.friendList, currentUser) }
            .map { createGroupForm }
    }

    private fun validateGroupMembers(members: HashSet<String>, friendList: List<Friend>, currentUser: String) {
        val usernames = friendList.stream().map { it.username }.collect(Collectors.toList())
        members.forEach { member ->
            if (!usernames.contains(member) && currentUser != member) {
                throw ValidationException("Error occurred during validating group creation form - only users from friend list can be added to group.")
            }
        }
    }
}
