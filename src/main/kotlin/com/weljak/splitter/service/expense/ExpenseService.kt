package com.weljak.splitter.service.expense

import com.weljak.splitter.domain.model.expense.Expense
import com.weljak.splitter.domain.model.expense.ExpenseStatus
import com.weljak.splitter.domain.model.expense.ExpenseType
import reactor.core.publisher.Mono

interface ExpenseService {
    fun findById(id: String): Mono<Expense>
    fun create(expense: Expense): Mono<Expense>
    fun settleUp(expense: Expense, username: String): Mono<Expense>
    fun delete(expense: Expense): Mono<Void>
    fun findAllByPaidBy(paidBy: String): Mono<List<Expense>>
    fun findAllByGroupName(groupName: String): Mono<List<Expense>>
    fun findAllByTypeAndPaidBy(type: ExpenseType, username: String): Mono<List<Expense>>
    fun findAllByStatusAndPaidBy(status: ExpenseStatus, username: String): Mono<List<Expense>>
    fun findAllCurrentUserUnsettledExpenses(username: String): Mono<List<Expense>>
}