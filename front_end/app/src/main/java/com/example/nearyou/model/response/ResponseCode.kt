package com.example.nearyou.model.response

enum class ResponseCode(code: String) {
    NO_ERROR_KNOWN(""),
    E_NO_INTERNET("E-NoInternet"),
    E_UNKNOWN_ROUTE("E-UnknownRoute"),
    E_UNKNOWN_ERROR("E-UnknownError"),
    E_NO_TOKEN("E-NoToken"),
    E_WRONG_CREDENTIALS("E-WrongCredentials"),
    E_UNAUTHORIZED("E-Unauthorized"),
    E_NO_RESOURCE("E-NoResource"),
    E_AGE_TOO_YOUNG("E-AgeTooYoung"),
    E_BAD_EMAIL_FORMAT("E-BadEmailFormat"),
    E_EMAIL_KNOWN("E-EmailKnown"),
    E_DATABASE_DATA_TOO_LONG("E-DatabaseDataTooLong"),
    E_EMAIL_TOO_LONG("E-EmailTooLong"),
    E_PASSWORD_TOO_LONG("E-PasswordTooLong"),
    E_PASSWORD_TOO_SHORT("E-PasswordTooShort"),
    E_SURNAME_TOO_LONG("E-SurnameTooLong"),
    E_FIRST_NAME_TOO_LONG("E-FirstNameTooLong"),
    S_SUCCESS("S-Success"),
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