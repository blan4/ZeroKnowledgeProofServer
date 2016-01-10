package org.seniorsigan.zkpauth.web.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.SessionToken
import org.seniorsigan.zkpauth.web.models.LoginToken
import org.seniorsigan.zkpauth.web.models.SignupToken

abstract class TokenGenerator(
    val objectMapper: ObjectMapper
) {
    abstract var domainName: String
    abstract val loginPath: String
    abstract val signupPath: String
    abstract val algorithm: String

    fun createLogin(st: SessionToken): LoginToken {
        return LoginToken(domainName, st.token, loginPath, st.expiresAt, algorithm)
    }

    fun createLoginJson(st: SessionToken): String {
        val token = createLogin(st)
        return objectMapper.writeValueAsString(token)
    }

    fun createSignup(st: SessionToken): SignupToken {
        return SignupToken(domainName, st.token, signupPath, st.expiresAt, algorithm)
    }

    fun createSignupJson(st: SessionToken): String {
        val token = createSignup(st)
        return objectMapper.writeValueAsString(token)
    }
}
