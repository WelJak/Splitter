package com.weljak.splitter.service.expense

import java.lang.RuntimeException

class UserNotPartOfExpenseException(message: String): RuntimeException(message) {
}