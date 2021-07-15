package com.weljak.splitter.domain.model.user

import java.util.*

data class User(
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: Role,
    val enabled: Boolean,
    val id: String? = UUID.randomUUID().toString()
)
