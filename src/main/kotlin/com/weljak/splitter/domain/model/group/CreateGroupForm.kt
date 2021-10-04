package com.weljak.splitter.domain.model.group

data class CreateGroupForm(
    val groupName: String,
    val members: Set<String> ?= null
)
