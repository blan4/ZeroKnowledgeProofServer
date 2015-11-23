package com.seniorsigan.qrauth.core.models

import java.sql.Timestamp
import java.util.*

data class LoginRequest(
    var id: Long = 0L,
    var sessionId: String = "",
    var token: String = "",
    var expiresAt: Timestamp = Timestamp(Date().time),
    var createdAt: Timestamp = Timestamp(Date().time),
    var updatedAt: Timestamp = Timestamp(Date().time)
)
