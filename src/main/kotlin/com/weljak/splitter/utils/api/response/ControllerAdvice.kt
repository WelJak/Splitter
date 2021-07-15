package com.weljak.splitter.utils.api.response

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.weljak.splitter.service.user.UserAlreadyExistsException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
class ControllerAdvice {
    private val log = KotlinLogging.logger { }

    @ExceptionHandler(JsonParseException::class, JsonMappingException::class)
    fun malformedRequestPayload(serverHttpRequest: ServerHttpRequest, exception: Exception): ResponseEntity<SplitterResponse> {
        log.warn("Malformed request payload: ${exception.message}")
        return SplitterResponseUtils.error(serverHttpRequest, null, "Malformed payload", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun userAlreadyExists(serverHttpRequest: ServerHttpRequest, exception: Exception): ResponseEntity<SplitterResponse> {
        val message = exception.message
        log.warn("User already exists: $message")
        return SplitterResponseUtils.error(serverHttpRequest, null, message.toString(), HttpStatus.BAD_REQUEST)
    }
}