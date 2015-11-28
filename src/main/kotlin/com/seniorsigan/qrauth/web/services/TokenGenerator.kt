package com.seniorsigan.qrauth.web.services

import com.seniorsigan.qrauth.web.models.LoginToken
import com.seniorsigan.qrauth.web.models.SignupToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenGenerator {
    @Value("\${domainName}")
    lateinit var domainName: String

    @Value("\${loginPath}")
    lateinit var loginPath: String

    @Value("\${signupPath}")
    lateinit var signupPath: String

    fun generateLogin(): LoginToken {
        val uuid = UUID.randomUUID().toString()
        val expiresAt = Date(Date().time + 1000 * 60)
        return LoginToken(domainName, uuid, loginPath, expiresAt)
    }

    fun generateSignup(): SignupToken {
        val uuid = UUID.randomUUID().toString()
        val expiresAt = Date(Date().time + 1000 * 180)
        return SignupToken(domainName, uuid, signupPath, expiresAt)
    }
}
