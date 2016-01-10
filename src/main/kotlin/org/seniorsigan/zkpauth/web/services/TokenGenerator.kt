package org.seniorsigan.zkpauth.web.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.LoginRequest
import org.seniorsigan.zkpauth.core.models.SignupRequest
import org.seniorsigan.zkpauth.core.repositories.LoginRequestRepository
import org.seniorsigan.zkpauth.core.repositories.SignupRequestRepository
import org.seniorsigan.zkpauth.web.models.LoginToken
import org.seniorsigan.zkpauth.web.models.SignupToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

abstract class TokenGenerator(
    val loginRequestRepository: LoginRequestRepository,
    val signupRequestRepository: SignupRequestRepository,
    val objectMapper: ObjectMapper
) {
    abstract var domainName: String
    abstract val loginPath: String
    abstract val signupPath: String
    abstract val algorithm: String

    fun generateLogin(): LoginToken {
        val uuid = UUID.randomUUID().toString()
        val expiresAt = Date(Date().time + 1000 * 60)
        return LoginToken(domainName, uuid, loginPath, expiresAt, algorithm)
    }

    fun generateSignup(): SignupToken {
        val uuid = UUID.randomUUID().toString()
        val expiresAt = Date(Date().time + 1000 * 180)
        return SignupToken(domainName, uuid, signupPath, expiresAt, algorithm)
    }

    fun createLogin(request: HttpServletRequest): LoginToken {
        val sessionId = request.session.id
        val token = generateLogin()
        val loginRequest = LoginRequest(sessionId = sessionId, token = token.token, expiresAt = token.expiresAt)
        loginRequestRepository.saveOrUpdate(loginRequest)
        return token
    }

    fun createLoginJson(request: HttpServletRequest): String {
        val token = createLogin(request)
        return objectMapper.writeValueAsString(token)
    }

    fun createSignup(request: HttpServletRequest): SignupToken {
        val sessionId = request.session.id
        val token = generateSignup()
        val signupRequest = SignupRequest(sessionId = sessionId, token = token.token, expiresAt = token.expiresAt)
        signupRequestRepository.saveOrUpdate(signupRequest)
        return token
    }

    fun createSignupJson(request: HttpServletRequest): String {
        val token = createSignup(request)
        return objectMapper.writeValueAsString(token)
    }
}
