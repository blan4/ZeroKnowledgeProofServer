package org.seniorsigan.zkpauth.web.models

data class LoginForm(
    var login: String = "",
    var key: String = "",
    var token: String = ""
)
