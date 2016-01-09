package org.seniorsigan.zkpauth.core.models

import java.sql.Timestamp
import java.util.*

data class User(
    var id: Long = 0L,
    var login: String = "",
    var algorithm: String = "",
    var secret: String = "",
    var createdAt: Timestamp = Timestamp(Date().time),
    var updatedAt: Timestamp = Timestamp(Date().time)
)
