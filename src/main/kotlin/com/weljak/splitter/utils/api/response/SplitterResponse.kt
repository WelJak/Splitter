package com.weljak.splitter.utils.api.response

import com.fasterxml.jackson.annotation.JsonInclude

data class SplitterResponse(
    val timestamp: String,
    val path: String,
    val statusCode: Int,
    val status: String,
    val success: Boolean,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val payload: Any? = null,
)