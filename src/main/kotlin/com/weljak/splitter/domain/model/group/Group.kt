package com.weljak.splitter.domain.model.group

import java.time.LocalDateTime

data class Group(
    val id: String,
    val groupName: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val members: List<String> ?= emptyList()
)
