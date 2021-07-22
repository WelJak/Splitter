package com.weljak.splitter.service.friends.request

import java.lang.RuntimeException

class RequestDoesNotExistException: RuntimeException("Request does not exist.") {
}