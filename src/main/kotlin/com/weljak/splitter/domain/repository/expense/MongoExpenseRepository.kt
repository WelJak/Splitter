package com.weljak.splitter.domain.repository.expense

import com.weljak.splitter.domain.model.expense.Expense
import com.weljak.splitter.domain.model.expense.ExpenseDocument
import com.weljak.splitter.domain.model.expense.ExpenseStatus
import com.weljak.splitter.domain.model.expense.ExpenseType
import com.weljak.splitter.utils.mapper.ExpenseMapper
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.and
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Repository
class MongoExpenseRepository(
    private val expenseMapper: ExpenseMapper,
    private val mongoExpenseDocumentRepository: MongoExpenseDocumentRepository,
    private val mongoTemplate: ReactiveMongoTemplate
): ExpenseRepository {
    override fun findById(id: String): Mono<Expense> {
        return mongoExpenseDocumentRepository.findById(id).map { expenseMapper.toExpense(it) }
    }

    override fun findAllByPaidBy(username: String): Mono<List<Expense>> {
        return mongoExpenseDocumentRepository.findAllByPaidBy(username).collectList().map { expenseMapper.toExpenses(it) }
    }

    override fun findAllByGroupName(groupName: String): Mono<List<Expense>> {
        return mongoExpenseDocumentRepository.findAllByGroupName(groupName).collectList().map { expenseMapper.toExpenses(it) }
    }

    override fun findAllByTypeAndPaidBy(type: ExpenseType, username: String): Mono<List<Expense>> {
        return mongoExpenseDocumentRepository.findAllByTypeAndPaidBy(type, username).collectList().map { expenseMapper.toExpenses(it) }
    }

    override fun findAllByStatusAndPaidBy(status: ExpenseStatus, username: String): Mono<List<Expense>> {
        return mongoExpenseDocumentRepository.findAllByStatusAndPaidBy(status, username).collectList().map { expenseMapper.toExpenses(it) }
    }

    override fun delete(expense: Expense): Mono<Void> {
        return mongoExpenseDocumentRepository.delete(expenseMapper.toExpenseDocument(expense))
    }

    override fun save(expense: Expense): Mono<Expense> {
        return Mono.just(expense)
            .flatMap { mongoExpenseDocumentRepository.save(expenseMapper.toExpenseDocument(it)).thenReturn(it) }
    }

    override fun update(expense: Expense, expenseBreakDown: Map<String, BigDecimal>, status: ExpenseStatus?): Mono<Expense> {
        val update = Update()
        update.set("expenseBreakdown", expenseBreakDown)
        update.set("status", status)
        return mongoTemplate.updateFirst(query(where("id").`is`(expense.id)), update, ExpenseDocument::class.java)
            .flatMap { mongoExpenseDocumentRepository.findById(expense.id) }
            .map { expenseMapper.toExpense(it) }
    }

    override fun findUsersAllUnsettledExpenses(username: String): Mono<List<Expense>> {
        val query = query(where("expenseBreakdown.$username").exists(true).and("status").`is`("NOT_SETTLED"))
        return mongoTemplate.find(query, ExpenseDocument::class.java)
            .map { expenseMapper.toExpense(it) }.collectList()
    }


}
