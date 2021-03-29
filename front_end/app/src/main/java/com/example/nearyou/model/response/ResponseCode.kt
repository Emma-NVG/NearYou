package com.example.nearyou.model.response

enum class ResponseCode(code: String) {
    E_UNKNOWN_ROUTE("E-UnknownRoute"),
    E_UNKNOWN_ERROR("E-UnknownError"),
    E_NO_TOKEN("E-NoToken"),
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
    S_SUCCESS("S-Success")
}