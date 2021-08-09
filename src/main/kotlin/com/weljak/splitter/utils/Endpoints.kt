package com.weljak.splitter.utils

object Endpoints {
    private const val USER_BASE_ENDPOINT = "/user"
    const val USER_DETAILS_ENDPOINT = "$USER_BASE_ENDPOINT/{username}"
    const val USER_REGISTER_ENDPOINT = "$USER_BASE_ENDPOINT/create"
    const val USER_LOGIN_ENDPOINT = "$USER_BASE_ENDPOINT/auth/login"

    const val FRIENDS_ENDPOINT = "/friends"
    private const val FRIENDS_REQUEST_ENDPOINT = "$FRIENDS_ENDPOINT/request"
    const val SENT_FRIEND_REQUESTS_ENDPOINT = "$FRIENDS_REQUEST_ENDPOINT/sent"
    const val RECEIVED_FRIEND_REQUESTS_ENDPOINT = "$FRIENDS_REQUEST_ENDPOINT/received"
    const val CREATE_FRIENDS_REQUEST_ENDPOINT = "$FRIENDS_REQUEST_ENDPOINT/create"
    const val CONFIRM_FRIENDS_REQUEST_ENDPOINT = "$FRIENDS_REQUEST_ENDPOINT/confirm/{reqId}/{confirmationId}"

    private const val EXPENSE_ENDPOINT = "/expense"
    const val CREATE_EXPENSE_ENDPOINT = "$EXPENSE_ENDPOINT/create"
    const val SETTLE_UP_EXPENSE_ENDPOINT = "$EXPENSE_ENDPOINT/settle/{id}"
    const val DELETE_EXPENSE_ENDPOINT = "$EXPENSE_ENDPOINT/delete/{id}"
    const val FIND_EXPENSES_ENDPOINT = "$EXPENSE_ENDPOINT/all"
    const val FIND_EXPENSES_BY_GROUP_NAME_ENDPOINT = "$EXPENSE_ENDPOINT/{groupName}"
    const val GET_CURRENT_USER_UNSETTLED_EXPENSES = "$FIND_EXPENSES_ENDPOINT/unsettled"

}
