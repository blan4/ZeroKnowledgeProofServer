package org.seniorsigan.zkpauth.core.models

import java.sql.Timestamp
import java.util.*

data class SKeyUser(
    var id: Long = 0L,
    var login: String = "",
    var secret: SKeySecret = SKeySecret(),
    var createdAt: Timestamp = Timestamp(Date().time),
    var updatedAt: Timestamp = Timestamp(Date().time)
) {
    val algorithm = SKeyUser.algorithm

    var token: String
        get() = secret.token
        set(value) { secret.token = value }
    var tokensUsed: Long
        get() = secret.tokensUsed
        set(value) { secret.tokensUsed = value }

    companion object {
        val algorithm = "s/key"
    }
}

data class SKeySecret(
    var token: String = "",
    var tokensUsed: Long = 0L
)
