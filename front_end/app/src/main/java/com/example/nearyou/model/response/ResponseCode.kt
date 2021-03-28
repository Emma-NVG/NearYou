package com.example.nearyou.model.response

enum class ResponseCode(code: String) {
    E_UNKNOWN_ROUTE("E-UnknownRoute"),
    E_UNKNOWN_ERROR("E-UnknownError"),
    E_NO_TOKEN("E-NoToken"),
    E_UNAUTHORIZED("E-Unauthorized"),
    E_NO_RESOURCE("E-NoResource"),
    S_SUCCESS("S-Success")
}