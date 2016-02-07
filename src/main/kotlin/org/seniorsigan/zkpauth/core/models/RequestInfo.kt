package org.seniorsigan.zkpauth.core.models

data class RequestInfo(
        var ip: String? = "",
        var host: String = "",
        var port: Int = -1,
        var userAgent: String = ""
)