package com.weljak.splitter.domain.model.expense

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

data class Expense(
    val id: String,
    val description: String,
    val createdBy: String,
    val paidBy: String,
    val amountPaidByPayer: BigDecimal,
    val totalSum: BigDecimal,
    val currency: Currency,
    val type: ExpenseType,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val groupName: String? = null,
    val expenseBreakdown: Map<String, BigDecimal>,
    val status: ExpenseStatus ?= ExpenseStatus.NOT_SETTLED
)
