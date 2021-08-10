package com.weljak.splitter.utils.api.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import reactor.core.publisher.Mono
import java.time.Clock
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object SplitterResponseUtils {
    private val clock: Clock = Clock.systemUTC()
    private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    fun success(
        serverHttpRequest: ServerHttpRequest,
        payload: Any? = null,
        message: String,
        httpsStatus: HttpStatus
    ): ResponseEntity<SplitterResponse> {
        val endpoint = serverHttpRequest.path.pathWithinApplication().value()
        val response = successsplitterResponse(endpoint, payload, message, httpsStatus)
        return ResponseEntity(response, httpsStatus)
    }

    fun error(
        serverHttpRequest: ServerHttpRequest,
        payload: Any? = null,
        message: String,
        httpsStatus: HttpStatus
    ): ResponseEntity<SplitterResponse> {
        val endpoint = serverHttpRequest.path.pathWithinApplication().value()
        val response = errorsplitterResponse(endpoint, payload, message, httpsStatus)
        return ResponseEntity(response, httpsStatus)
    }

    fun empty(serverHttpRequest: ServerHttpRequest, payload: Any?, message: String, httpStatus: HttpStatus): ResponseEntity<SplitterResponse> {
        val endpoint = serverHttpRequest.path.pathWithinApplication().value()
        val response = successsplitterResponse(endpoint, payload, message, httpStatus)
        return ResponseEntity(response, httpStatus)
    }

    fun noContent(serverHttpRequest: ServerHttpRequest, message: String): ResponseEntity<SplitterResponse> {
        val endpoint = serverHttpRequest.path.pathWithinApplication().value()
        val response = successsplitterResponse(endpoint, null, message, HttpStatus.NO_CONTENT)
        return ResponseEntity(response, HttpStatus.OK)
    }

    private fun successsplitterResponse(endpoint: String, payload: Any?, message: String, httpsStatus: HttpStatus): SplitterResponse {
        return SplitterResponse(
            getTimestamp(),
            endpoint,
            httpsStatus.value(),
            httpsStatus.name,
            true,
            message,
            payload
        )
    }

    private fun errorsplitterResponse(endpoint: String, payload: Any?, message: String, httpsStatus: HttpStatus): SplitterResponse {
        return SplitterResponse(
            getTimestamp(),
            endpoint,
            httpsStatus.value(),
            httpsStatus.name,
            false,
            message,
            payload
        )
    }

    private fun getTimestamp(): String {
        val now = OffsetDateTime.now(clock)
        return FORMATTER.format(now)
    }
}