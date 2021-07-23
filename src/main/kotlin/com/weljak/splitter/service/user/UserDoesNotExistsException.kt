package com.weljak.splitter.service.user

import java.lang.RuntimeException

class UserDoesNotExistsException(message: String): RuntimeException(message)
