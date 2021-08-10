package com.weljak.splitter.webapi.controller.expense

import com.weljak.splitter.domain.model.expense.ExpenseStatus
import com.weljak.splitter.domain.model.expense.ExpenseType
import com.weljak.splitter.service.expense.ExpenseService
import com.weljak.splitter.utils.Endpoints
import com.weljak.splitter.utils.api.response.SplitterResponse
import com.weljak.splitter.utils.api.response.SplitterResponseUtils
import com.weljak.splitter.utils.mapper.ExpenseMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ExpenseController(
    private val expenseService: ExpenseService
) {
    @PostMapping(Endpoints.CREATE_EXPENSE_ENDPOINT)
    fun createExpense(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @RequestBody expenseCreationForm: ExpenseCreationForm
    ): Mono<ResponseEntity<SplitterResponse>> {
        return Mono.just(expenseCreationForm)
            .map { ExpenseMapper.toExpense(it, currentUser) }
            .flatMap { expenseService.create(it) }
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Expense created", HttpStatus.CREATED) }
            .onErrorResume {
                Mono.just(
                    SplitterResponseUtils.error(
                        serverHttpRequest,
                        null,
                        "Error during creating expense: ${it.message.toString()}",
                        HttpStatus.BAD_REQUEST)
                )
            }
    }

    @PutMapping(Endpoints.SETTLE_UP_EXPENSE_ENDPOINT)
    fun settleUpExpense(serverHttpRequest: ServerHttpRequest,
                        @AuthenticationPrincipal currentUser: String,
                        @PathVariable id: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return expenseService.findById(id)
            .flatMap { expenseService.settleUp(it, currentUser) }
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "Expense settled up", HttpStatus.OK) }
            .onErrorResume {
                Mono.just(
                    SplitterResponseUtils.error(
                        serverHttpRequest,
                        null,
                        "Error during settling up expense: ${it.message.toString()}",
                        HttpStatus.BAD_REQUEST)
                )
            }
            .switchIfEmpty(Mono.just(SplitterResponseUtils.success(serverHttpRequest, null, "Expense not found", HttpStatus.OK)))
    }

    @DeleteMapping(Endpoints.DELETE_EXPENSE_ENDPOINT)
    fun deleteExpense(serverHttpRequest: ServerHttpRequest,
                      @AuthenticationPrincipal currentUser: String,
                      @PathVariable id: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return expenseService.findById(id)
            .flatMap { expenseService.delete(it) }
            .flatMap { SplitterResponseUtils.noContent() }
    }

    @GetMapping(Endpoints.FIND_EXPENSES_ENDPOINT)
    fun getExpenses(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @RequestParam(required = true) paidBy: String,
        @RequestParam(required = false) type: ExpenseType?,
        @RequestParam(required = false) status: ExpenseStatus?,
    ): Mono<ResponseEntity<SplitterResponse>> {
        return Mono.just(paidBy)
            .flatMap {
                if (type != null && status == null) {
                    expenseService.findAllByTypeAndPaidBy(type, paidBy)
                        .map { SplitterResponseUtils.success(serverHttpRequest, it, "Expense fetched", HttpStatus.OK) }
                } else if (type == null && status != null) {
                    expenseService.findAllByStatusAndPaidBy(status, paidBy)
                        .map { SplitterResponseUtils.success(serverHttpRequest, it, "Expense fetched", HttpStatus.OK) }
                } else if (type == null && status == null) {
                    expenseService.findAllByPaidBy(paidBy)
                        .map { SplitterResponseUtils.success(serverHttpRequest, it, "Expense fetched", HttpStatus.OK) }
                } else {
                    Mono.just(SplitterResponseUtils.error(serverHttpRequest, null, "Error during fetching expense", HttpStatus.BAD_REQUEST))
                }
            }
            .map { it }
    }

    @GetMapping(Endpoints.FIND_EXPENSES_BY_GROUP_NAME_ENDPOINT)
    fun getExpensesByGroupName(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String,
        @PathVariable groupName: String
    ): Mono<ResponseEntity<SplitterResponse>> {
        return expenseService.findAllByGroupName(groupName)
            .map { SplitterResponseUtils.success(serverHttpRequest, it, "All group expenses fetched", HttpStatus.OK) }
    }

    @GetMapping(Endpoints.GET_CURRENT_USER_UNSETTLED_EXPENSES)
    fun getCurrentUserUnsettledExpenses(
        serverHttpRequest: ServerHttpRequest,
        @AuthenticationPrincipal currentUser: String
    ): Mono<ResponseEntity<SplitterResponse>> {
       return expenseService.findAllCurrentUserUnsettledExpenses(currentUser)
           .map { SplitterResponseUtils.success(serverHttpRequest, it, "All unsettled group expenses fetched", HttpStatus.OK) }
    }
}
