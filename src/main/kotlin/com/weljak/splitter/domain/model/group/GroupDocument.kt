package com.weljak.splitter.domain.model.group

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "group")
data class GroupDocument (
    val id: String,
    val groupName: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val members: List<String> ?= emptyList()
)