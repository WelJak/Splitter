package com.weljak.splitter.webapi.controller.expense

import com.weljak.splitter.domain.model.expense.Currency
import com.weljak.splitter.domain.model.expense.ExpenseType
import java.math.BigDecimal

data class ExpenseCreationForm(
    val description: String,
    val paidBy: String,
    val amountPaidByPayer: BigDecimal,
    val totalSum: BigDecimal,
    val currency: Currency,
    val type: ExpenseType,
    val groupName: String? = null,
    val expenseBreakdown: Map<String, BigDecimal>,
)
