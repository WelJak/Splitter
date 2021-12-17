package com.weljak.splitter.webapi.controller.group

import com.weljak.splitter.service.group.GroupService
import com.weljak.splitter.utils.Endpoints
import com.weljak.splitter.utils.api.response.SplitterResponse
import com.weljak.splitter.utils.api.response.SplitterResponseUtils
import com.weljak.splitter.utils.mapper.GroupMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
class GroupController(
    private val groupService: GroupService,
    private val groupMapper: GroupMapper
) {
    @PostMapping(Endpoints.CREATE_GROUP_ENDPOINT)
    fun createGroup(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @RequestBody createGroupForm: CreateGroupForm
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.createGroup(createGroupForm, currentUser)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Group created", HttpStatus.CREATED) }
            .onErrorResume {
                Mono.just(
                    SplitterResponseUtils.error(
                        serverHttpRequest,
                        null,
                        "Error occurred during creating group: ${it.message}",
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @GetMapping(Endpoints.GET_CURRENT_USER_GROUPS_ENDPOINT)
    fun getCurrentUserGroups(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.getCurrentUserGroups(currentUser)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Current user groups fetched", HttpStatus.OK) }
    }

    @GetMapping(Endpoints.FIND_GROUP_BY_ID_ENDPOINT)
    fun getGroupById(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable id: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.findById(id)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Group details fetched", HttpStatus.OK) }
    }

    @DeleteMapping(Endpoints.DELETE_GROUP_ENDPOINT)
    fun deleteGroupById(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable id: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.deleteById(id, currentUser)
            .map { SplitterResponseUtils.noContent(serverHttpRequest, "GroupDeleted") }
    }

    @GetMapping(Endpoints.FIND_GROUPS_BY_NAME_ENDPOINT)
    fun findGroupsByName(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable name: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.findByGroupName(name)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Groups details fetched", HttpStatus.OK) }
    }

    @PutMapping(Endpoints.ADD_USERS_TO_GROUP_ENDPOINT)
    fun addUsersToGroup(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable id: String,
        @RequestBody toAdd: ManageGroupMembershipRequest
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.findById(id)
            .flatMap { groupService.addMembers(groupMapper.toGroupDocument(it), toAdd.usernames, currentUser) }
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Users added to group", HttpStatus.OK) }
    }

    @DeleteMapping(Endpoints.REMOVE_MEMBERS_FROM_GROUP_ENDPOINT)
    fun removeUsersFromGroup(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable id: String,
        @RequestBody toDelete: ManageGroupMembershipRequest
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.findById(id)
            .flatMap { groupService.removeMembers(groupMapper.toGroupDocument(it), toDelete.usernames, currentUser) }
            .map { SplitterResponseUtils.noContent(serverHttpRequest, "Users removed from group") }
    }

    @GetMapping(Endpoints.LEAVE_GROUP_ENDPOINT)
    fun leaveGroup(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable id: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.findById(id)
            .flatMap { groupService.leaveGroup(currentUser, groupMapper.toGroupDocument(it)) }.thenReturn(SplitterResponseUtils.noContent(serverHttpRequest, "Group has been left"))
    }
}