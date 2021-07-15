package com.weljak.splitter.domain.model.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class UserDocument(
    @Id
    val id: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: Role,
    val enabled: Boolean
)