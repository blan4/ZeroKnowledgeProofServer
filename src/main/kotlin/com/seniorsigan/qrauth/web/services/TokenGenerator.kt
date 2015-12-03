package com.seniorsigan.qrauth.web.services

import com.seniorsigan.qrauth.core.models.LoginRequest
import com.seniorsigan.qrauth.core.models.SignupRequest
import com.seniorsigan.qrauth.core.repositories.LoginRequestRepository
import com.seniorsigan.qrauth.core.repositories.SignupRequestRepository
import com.seniorsigan.qrauth.web.models.LoginToken
import com.seniorsigan.qrauth.web.models.SignupToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class TokenGenerator
@Autowired constructor(
    val loginRequestRepository: LoginRequestRepository,
    val signupRequestRepository: SignupRequestRepository
) {
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

    fun createLogin(request: HttpServletRequest): LoginToken {
        val sessionId = request.requestedSessionId
        val token = generateLogin()
        val loginRequest = LoginRequest(sessionId = sessionId, token = token.token, expiresAt = token.expiresAt)
        loginRequestRepository.saveOrUpdate(loginRequest)
        return token
    }

    fun createSignup(request: HttpServletRequest): SignupToken {
        val sessionId = request.requestedSessionId
        val token = generateSignup()
        val signupRequest = SignupRequest(sessionId = sessionId, token = token.token, expiresAt = token.expiresAt)
        signupRequestRepository.saveOrUpdate(signupRequest)
        return token
    }
}
