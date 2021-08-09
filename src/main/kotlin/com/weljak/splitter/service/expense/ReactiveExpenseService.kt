package com.weljak.splitter.service.expense

import com.weljak.splitter.domain.model.expense.Expense
import com.weljak.splitter.domain.model.expense.ExpenseStatus
import com.weljak.splitter.domain.model.expense.ExpenseType
import com.weljak.splitter.domain.repository.expense.ExpenseRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Service
class ReactiveExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val expenseValidator: ExpenseValidator
): ExpenseService {
    override fun findById(id: String): Mono<Expense> {
        return expenseRepository.findById(id)
    }

    override fun create(expense: Expense): Mono<Expense> {
        return expenseValidator.validateExpense(expense)
            .flatMap { expenseRepository.save(it) }
    }

    override fun settleUp(expense: Expense, username: String): Mono<Expense> {
        if (!expense.expenseBreakdown.keys.contains(username)) {
            throw UserNotPartOfExpenseException("Given user is not part of expense")
        }
        val updated = HashMap(expense.expenseBreakdown)
        updated[username] = BigDecimal.ZERO
        if (updated.values.stream().reduce(BigDecimal::add).get() == BigDecimal.ZERO) {
            return expenseRepository.update(expense, updated, ExpenseStatus.SETTLED)
        }
        return expenseRepository.update(expense, updated)
    }

    override fun delete(expense: Expense): Mono<Void> {
        return expenseRepository.delete(expense)
    }

    override fun findAllByPaidBy(paidBy: String): Mono<List<Expense>> {
        return expenseRepository.findAllByPaidBy(paidBy)
    }

    override fun findAllByGroupName(groupName: String): Mono<List<Expense>> {
        return expenseRepository.findAllByGroupName(groupName)
    }

    override fun findAllByTypeAndPaidBy(type: ExpenseType, username: String): Mono<List<Expense>> {
        return expenseRepository.findAllByTypeAndPaidBy(type, username)
    }

    override fun findAllByStatusAndPaidBy(status: ExpenseStatus, username: String): Mono<List<Expense>> {
        return expenseRepository.findAllByStatusAndPaidBy(status, username)
    }

    override fun findAllCurrentUserUnsettledExpenses(username: String): Mono<List<Expense>> {
        return expenseRepository.findUsersAllUnsettledExpenses(username)
    }
}