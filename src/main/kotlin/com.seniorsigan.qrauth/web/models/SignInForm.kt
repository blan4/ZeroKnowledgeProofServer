package com.seniorsigan.qrauth.web.models

data class SignInForm(
    var login: String = "",
    var key: String = "",
    var token: String = ""
)
