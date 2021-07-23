package com.weljak.splitter.utils

object Endpoints {
    private const val USER_BASE_ENDPOINT = "/user"
    const val USER_DETAILS_ENDPOINT = "$USER_BASE_ENDPOINT/{username}"
    const val USER_REGISTER_ENDPOINT = "$USER_BASE_ENDPOINT/create"
    const val USER_LOGIN_ENDPOINT = "$USER_BASE_ENDPOINT/auth/login"

    const val FRIENDS_ENDPOINT = "/friends"
    private const val FRIENDS_REQUEST_ENDPOINT = "$FRIENDS_ENDPOINT/request"
    const val CREATE_FRIENDS_REQUEST_ENDPOINT = "$FRIENDS_REQUEST_ENDPOINT/create"
    const val CONFIRM_FRIENDS_REQUEST_ENDPOINT = "$FRIENDS_REQUEST_ENDPOINT/confirm/{reqId}/{confirmationId}"

}
