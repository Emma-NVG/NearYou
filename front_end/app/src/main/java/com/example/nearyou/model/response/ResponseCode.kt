package com.example.nearyou.model.response

enum class ResponseCode {
    NO_ERROR_KNOWN,
    E_NO_INTERNET,
    E_UNKNOWN_ROUTE,
    E_UNKNOWN_ERROR,
    E_NO_TOKEN,
    E_WRONG_CREDENTIALS,
    E_UNAUTHORIZED,
    E_NO_RESOURCE,
    E_AGE_TOO_YOUNG,
    E_BAD_EMAIL_FORMAT,
    E_EMAIL_KNOWN,
    E_DATABASE_DATA_TOO_LONG,
    E_EMAIL_TOO_LONG,
    E_PASSWORD_TOO_LONG,
    E_PASSWORD_TOO_SHORT,
    E_SURNAME_TOO_LONG,
    E_FIRST_NAME_TOO_LONG,
    S_SUCCESS,
}

class ResponseCodeBuilder {
    companion object {
        fun getResponseCodeInstance(code: String): ResponseCode {
            return when (code) {
                "E-NoInternet" -> ResponseCode.E_NO_INTERNET
                "E-UnknownRoute" -> ResponseCode.E_UNKNOWN_ROUTE
                "E-UnknownError" -> ResponseCode.E_UNKNOWN_ERROR
                "E-NoToken" -> ResponseCode.E_NO_TOKEN
                "E-WrongCredentials" -> ResponseCode.E_WRONG_CREDENTIALS
                "E-Unauthorized" -> ResponseCode.E_UNAUTHORIZED
                "E-NoResource" -> ResponseCode.E_NO_RESOURCE
                "E-AgeTooYoung" -> ResponseCode.E_AGE_TOO_YOUNG
                "E-BadEmailFormat" -> ResponseCode.E_BAD_EMAIL_FORMAT
                "E-EmailKnown" -> ResponseCode.E_EMAIL_KNOWN
                "E-DatabaseDataTooLong" -> ResponseCode.E_DATABASE_DATA_TOO_LONG
                "E-EmailTooLong" -> ResponseCode.E_EMAIL_TOO_LONG
                "E-PasswordTooLong" -> ResponseCode.E_PASSWORD_TOO_LONG
                "E-PasswordTooShort" -> ResponseCode.E_PASSWORD_TOO_SHORT
                "E-SurnameTooLong" -> ResponseCode.E_SURNAME_TOO_LONG
                "E-FirstNameTooLong" -> ResponseCode.E_FIRST_NAME_TOO_LONG
                "S-Success" -> ResponseCode.S_SUCCESS
                else -> ResponseCode.NO_ERROR_KNOWN
            }
        }
    }
}