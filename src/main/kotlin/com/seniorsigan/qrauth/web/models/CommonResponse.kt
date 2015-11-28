package com.seniorsigan.qrauth.web.models

data class CommonResponse(
    val success: Boolean = false,
    val error: String = "",
    val message: String = ""
)
