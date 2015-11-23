package com.seniorsigan.qrauth.core.repositories

import com.seniorsigan.qrauth.core.mappers.LoginRequestMapper
import com.seniorsigan.qrauth.core.models.LoginRequest
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

    open fun delete(loginRequest: LoginRequest) {
        return mapper.delete(loginRequest)
    }
}
