package com.seniorsigan.qrauth.core.models

import java.util.*

data class SignupRequest(
    var id: Long = 0L,
    var sessionId: String = "",
    var token: String = "",
    var expiresAt: Date = Date(),
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
)
