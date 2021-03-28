package com.example.nearyou.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseBody<T>(val message: String,
                           private val responseCode: String,
                           val data: T) {
    val code get() = ResponseCode.valueOf(responseCode)
}