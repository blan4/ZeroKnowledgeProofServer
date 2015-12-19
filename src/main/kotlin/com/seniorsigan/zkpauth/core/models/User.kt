package com.seniorsigan.zkpauth.core.models

import java.sql.Timestamp
import java.util.*

data class User(
    var id: Long = 0L,
    var login: String = "",
    var token: String? = null,
    var tokensUsed: Long = 0L,
    var createdAt: Timestamp = Timestamp(Date().time),
    var updatedAt: Timestamp = Timestamp(Date().time)
)
