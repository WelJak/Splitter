package com.weljak.splitter.domain.repository.expense

import com.weljak.splitter.domain.model.expense.Expense
import com.weljak.splitter.domain.model.expense.ExpenseStatus
import com.weljak.splitter.domain.model.expense.ExpenseType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface ExpenseRepository {
    fun findById(id: String): Mono<Expense>
    fun findAllByPaidBy(username: String): Mono<List<Expense>>
    fun findAllByGroupName(groupName: String): Mono<List<Expense>>
    fun findAllByTypeAndPaidBy(type: ExpenseType, username: String): Mono<List<Expense>>
    fun findAllByStatusAndPaidBy(status: ExpenseStatus, username: String): Mono<List<Expense>>
    fun delete(expense: Expense): Mono<Void>
    fun save(expense: Expense): Mono<Expense>
    fun update(expense: Expense, expenseBreakDown: Map<String, BigDecimal>, status: ExpenseStatus ?= ExpenseStatus.NOT_SETTLED): Mono<Expense>
    fun findUsersAllUnsettledExpenses(username: String): Mono<List<Expense>>
}