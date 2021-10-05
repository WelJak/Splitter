package com.weljak.splitter.webapi.controller.group

data class CreateGroupForm(
    val groupName: String,
    val members: HashSet<String> ?= HashSet()
)
