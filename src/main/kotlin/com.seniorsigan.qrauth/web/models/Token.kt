package com.seniorsigan.qrauth.web.models

import java.util.*

data class Token(
    val domainName: String,
    val requestToken: String,
    val loginPath: String,
    val expiresAt: Date
)
