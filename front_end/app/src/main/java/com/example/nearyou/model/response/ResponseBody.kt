package com.example.nearyou.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBody<T>(
    val message: String,
    @SerialName("code")
    private val _code: String,
    val data: T
) {

    val code: ResponseCode
        get() = ResponseCodeBuilder.getResponseCodeInstance(_code)
}