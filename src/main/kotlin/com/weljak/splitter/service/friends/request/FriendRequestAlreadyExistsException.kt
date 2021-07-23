package com.weljak.splitter.service.friends.request

import java.lang.RuntimeException

class FriendRequestAlreadyExistsException(message: String): RuntimeException(message) {
}