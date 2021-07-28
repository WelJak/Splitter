package com.weljak.splitter.service.friends

import java.lang.RuntimeException

class FriendshipAlreadyExistsException(message: String): RuntimeException(message)