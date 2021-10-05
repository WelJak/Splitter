package com.weljak.splitter.service.group

import java.lang.RuntimeException

class UserNotOwnerOfGroupException(message: String): RuntimeException(message)