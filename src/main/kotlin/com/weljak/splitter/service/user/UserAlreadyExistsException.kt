package com.weljak.splitter.service.user

import java.lang.RuntimeException

class UserAlreadyExistsException(message: String) : RuntimeException(message)