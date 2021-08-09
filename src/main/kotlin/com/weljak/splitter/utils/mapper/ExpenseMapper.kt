package com.weljak.splitter.utils.mapper

import com.weljak.splitter.domain.model.expense.Expense
import com.weljak.splitter.domain.model.expense.ExpenseDocument
import com.weljak.splitter.domain.model.expense.ExpenseStatus
import com.weljak.splitter.utils.mapper.config.GlobalMapperConfig
import com.weljak.splitter.webapi.controller.expense.ExpenseCreationForm
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.springframework.stereotype.Component
import java.util.*

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, config = GlobalMapperConfig::class)
interface ExpenseMapper {
    fun toExpense(expenseDocument: ExpenseDocument): Expense
    fun toExpenses(expenseDocuments: List<ExpenseDocument>): List<Expense>
    fun toExpenseDocument(expense: Expense): ExpenseDocument

    companion object {
        fun toExpense(expenseCreationForm: ExpenseCreationForm, creator: String): Expense {
            return Expense(
                UUID.randomUUID().toString(),
                creator,
                expenseCreationForm.paidBy,
                expenseCreationForm.amountPaidByPayer,
                expenseCreationForm.totalSum,
                expenseCreationForm.currency,
                expenseCreationForm.type,
                expenseCreationForm.groupName,
                expenseCreationForm.expenseBreakdown,
                ExpenseStatus.NOT_SETTLED
            )
        }
    }
}