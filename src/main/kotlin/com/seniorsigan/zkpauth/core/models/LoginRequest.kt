package com.seniorsigan.zkpauth.core.models

import java.util.*

data class LoginRequest(
    var id: Long = 0L,
    var sessionId: String = "",
    var token: String = "",
    var expiresAt: Date = Date(),
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
)
