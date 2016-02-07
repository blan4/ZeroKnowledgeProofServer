package org.seniorsigan.zkpauth.web.models

import org.seniorsigan.zkpauth.core.models.RequestInfo
import java.util.*

data class SignupToken(
    val domainName: String,
    val token: String,
    val path: String,
    val expiresAt: Date,
    val algorithm: String,
    val requestInfo: RequestInfo
) {
    val type: String = "SIGNUP"
}
