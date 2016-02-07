package org.seniorsigan.zkpauth.core.models

import java.util.*

data class SessionToken(
    var id: Long = 0L,
    var sessionId: String = "",
    var token: String = "",
    var meta: String? = null,
    var requestInfo: RequestInfo = RequestInfo(),
    var expiresAt: Date = Date(),
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
)
