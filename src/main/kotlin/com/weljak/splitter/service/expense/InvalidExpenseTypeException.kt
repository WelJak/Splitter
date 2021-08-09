package com.weljak.splitter.service.expense

import java.lang.RuntimeException

class InvalidExpenseTypeException(message: String): RuntimeException(message) {
}