package com.weljak.splitter.domain.repository.expense

import com.weljak.splitter.domain.model.expense.ExpenseDocument
import com.weljak.splitter.domain.model.expense.ExpenseStatus
import com.weljak.splitter.domain.model.expense.ExpenseType
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface MongoExpenseDocumentRepository: ReactiveMongoRepository<ExpenseDocument, String> {
    fun findAllByPaidBy(username: String): Flux<ExpenseDocument>
    fun findAllByGroupName(groupName: String): Flux<ExpenseDocument>
    fun findAllByTypeAndPaidBy(type: ExpenseType, username: String): Flux<ExpenseDocument>
    fun findAllByStatusAndPaidBy(status: ExpenseStatus, username: String): Flux<ExpenseDocument>
}