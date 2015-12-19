package com.seniorsigan.zkpauth.core.repositories

import com.seniorsigan.zkpauth.core.mappers.LoginRequestMapper
import com.seniorsigan.zkpauth.core.models.LoginRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class LoginRequestRepository
@Autowired constructor(val mapper: LoginRequestMapper) {
    open fun save(loginRequest: LoginRequest) {
        mapper.save(loginRequest)
    }

    open fun findAll(): List<LoginRequest> {
        return mapper.findAll()
    }

    open fun findByToken(token: String): LoginRequest? {
        return mapper.findByToken(token)
    }

    open fun findBySession(sessionId: String): LoginRequest? {
        return mapper.findBySession(sessionId)
    }

    open fun delete(loginRequest: LoginRequest) {
        return mapper.delete(loginRequest)
    }

    open fun saveOrUpdate(loginRequest: LoginRequest) {
        val savedLoginRequest = findBySession(loginRequest.sessionId)
        if (savedLoginRequest != null) {
            loginRequest.id = savedLoginRequest.id
            update(loginRequest)
        } else {
            save(loginRequest)
        }
    }

    open fun update(loginRequest: LoginRequest) {
        return mapper.update(loginRequest)
    }
}
