package com.weljak.splitter.domain.model.expense

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document("expenses")
data class ExpenseDocument(
    val id: String,
    val description: String,
    val createdBy: String,
    val paidBy: String,
    val amountPaidByPayer: BigDecimal,
    val totalSum: BigDecimal,
    val currency: Currency,
    val type: ExpenseType,
    val groupName: String? = null,
    val expenseBreakdown: Map<String, BigDecimal>,
    val status: ExpenseStatus
)
