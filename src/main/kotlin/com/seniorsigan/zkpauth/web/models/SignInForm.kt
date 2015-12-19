package com.seniorsigan.zkpauth.web.models

data class SignInForm(
    var login: String = "",
    var key: String = "",
    var token: String = ""
)
