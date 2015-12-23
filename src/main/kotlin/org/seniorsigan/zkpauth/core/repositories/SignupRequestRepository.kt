package org.seniorsigan.zkpauth.core.repositories

import org.seniorsigan.zkpauth.core.mappers.SignupRequestMapper
import org.seniorsigan.zkpauth.core.models.SignupRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SignupRequestRepository
@Autowired constructor(val mapper: SignupRequestMapper) {
    open fun save(signupRequest: SignupRequest) {
        mapper.save(signupRequest)
    }

    open fun findAll(): List<SignupRequest> {
        return mapper.findAll()
    }

    open fun findByToken(token: String): SignupRequest? {
        return mapper.findByToken(token)
    }

    open fun findBySession(sessionId: String): SignupRequest? {
        return mapper.findBySession(sessionId)
    }

    open fun delete(signupRequest: SignupRequest) {
        return mapper.delete(signupRequest)
    }

    open fun saveOrUpdate(signupRequest: SignupRequest) {
        val savedSignupRequest = findBySession(signupRequest.sessionId)
        if (savedSignupRequest != null) {
            signupRequest.id = savedSignupRequest.id
            update(signupRequest)
        } else {
            save(signupRequest)
        }
    }

    open fun update(signupRequest: SignupRequest) {
        return mapper.update(signupRequest)
    }
}
