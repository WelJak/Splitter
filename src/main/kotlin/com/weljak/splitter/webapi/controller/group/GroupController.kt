package com.weljak.splitter.webapi.controller.group

import com.weljak.splitter.domain.model.group.CreateGroupForm
import com.weljak.splitter.service.group.GroupService
import com.weljak.splitter.utils.Endpoints
import com.weljak.splitter.utils.api.response.SplitterResponse
import com.weljak.splitter.utils.api.response.SplitterResponseUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class GroupController(
    private val groupService: GroupService
) {
    @PostMapping(Endpoints.CREATE_GROUP_ENDPOINT)
    fun createGroup(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @RequestBody createGroupForm: CreateGroupForm
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.createGroup(createGroupForm, currentUser)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Group created", HttpStatus.CREATED) }
    }

    @GetMapping(Endpoints.GET_CURRENT_USER_GROUPS_ENDPOINT)
    fun getCurrentUserGroups(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return groupService.getCurrentUserGroups(currentUser)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Current user groups fetched", HttpStatus.OK) }
    }
}