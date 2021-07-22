package com.weljak.splitter.webapi.controller.user


data class UserCreationForm(
    val username: String,
    val email: String,
    val password: String,
)
