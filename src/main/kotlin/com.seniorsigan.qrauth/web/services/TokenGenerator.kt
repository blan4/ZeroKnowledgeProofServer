package com.seniorsigan.qrauth.web.services

import com.seniorsigan.qrauth.web.models.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenGenerator {
    @Value("\${domainName}")
    lateinit var domainName: String

    @Value("\${loginPath}")
    lateinit var loginPath: String

    fun generate(): Token {
        val uuid = UUID.randomUUID().toString()
        val expiresAt = Date(Date().time + 1000 * 60)
        return Token(domainName, uuid, loginPath, expiresAt)
    }
}
