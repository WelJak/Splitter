package com.weljak.splitter.service.expense

import java.lang.RuntimeException

class InvalidExpenseSumException(message: String): RuntimeException(message) {
}