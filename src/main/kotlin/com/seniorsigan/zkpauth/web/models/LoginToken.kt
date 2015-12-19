package com.seniorsigan.zkpauth.web.models

import java.util.*

data class LoginToken(
    val domainName: String,
    val token: String,
    val path: String,
    val expiresAt: Date,
    val type: String = "LOGIN"
)
