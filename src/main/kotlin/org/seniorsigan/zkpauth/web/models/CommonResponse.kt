package org.seniorsigan.zkpauth.web.models

data class CommonResponse(
    val success: Boolean = false,
    val error: String = "",
    val message: String = ""
)
