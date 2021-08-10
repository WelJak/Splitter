package com.weljak.splitter.service.expense

import com.weljak.splitter.domain.model.expense.Expense
import com.weljak.splitter.domain.model.expense.ExpenseType
import com.weljak.splitter.domain.repository.user.MongoUserDocumentRepository
import com.weljak.splitter.domain.repository.user.UserRepository
import com.weljak.splitter.service.user.UserDoesNotExistsException
import kotlin.math.exp
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.lang.RuntimeException
import java.math.BigDecimal
import java.util.function.BiFunction

@Component
class ExpenseValidator(private val userRepository: MongoUserDocumentRepository) {
    fun validateExpense(expense: Expense): Mono<Expense> {
        return Mono.just(expense)
            .zipWhen({validateExpenseSums(it)}, {it, valid -> Pair(it, valid)})
            .doOnNext { if (!it.second) {
                throw InvalidExpenseSumException("Expense sum is invalid")
            } }
            .map { it.first }
            .zipWhen({validateExpenseType(it)}, {it, valid -> Pair(it, valid)})
            .doOnNext { if (!it.second) {
                throw InvalidExpenseTypeException("Invalid expense type (eg. selected NON_GROUP expense then provided group name)")
            } }
            .map { it.first }
            .flatMap { validateExpenseUsers(it) }

    }

    private fun validateExpenseUsers(expense: Expense): Mono<Expense> {
        return Flux.fromIterable(expense.expenseBreakdown.keys)
            .map { key -> userRepository.existsByUsername(key).map { Pair(key, it) } }
            .flatMap { it }
            .collectList()
            .map {
                it.forEach { pair -> if (pair.second == false){
                    throw UserDoesNotExistsException("User ${pair.first} in given expense does not exist")
                } }
            }.thenReturn(expense)
            .map { it }
    }

    private fun validateExpenseSums(expense: Expense): Mono<Boolean> {
        return Mono.just(expense)
            .filter { it.totalSum > BigDecimal.valueOf(0) && it.amountPaidByPayer >= BigDecimal.valueOf(0) }
            .map {
                it.expenseBreakdown.values.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO).add(it.amountPaidByPayer)
            }
            .filter { it == expense.totalSum }
            .map { true }
            .switchIfEmpty(Mono.just(false))
    }

    private fun validateExpenseType(expense: Expense): Mono<Boolean> {
        return Mono.just(expense)
            .map { checkIfExpenseTypeAndGroupNameIsCorrect(it) }
    }

    private fun checkIfExpenseTypeAndGroupNameIsCorrect(expense: Expense): Boolean {
        if (expense.type == ExpenseType.NON_GROUP && expense.groupName != null) {
            return false
        } else if (expense.type == ExpenseType.GROUP && expense.groupName == null){
            return false
        }
        return true
    }
}
