package com.weljak.splitter.security

import com.weljak.splitter.domain.model.user.Role
import com.weljak.splitter.domain.model.user.User
import org.springframework.security.core.authority.AuthorityUtils

class CurrentUser constructor(val user: User) : org.springframework.security.core.userdetails.User(
    user.username,
    user.passwordHash,
    AuthorityUtils.createAuthorityList(user.role.toString())
) {
    fun getId(): String {
        return user.id!!
    }

    fun getRole(): Role {
        return user.role
    }

    fun getNickname(): String {
        return user.username
    }

    override fun isEnabled(): Boolean {
        return user.enabled
    }
}