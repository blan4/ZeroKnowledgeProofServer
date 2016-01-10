package org.seniorsigan.zkpauth.core.repositories

import org.seniorsigan.zkpauth.core.mappers.SessionTokenMapper
import org.seniorsigan.zkpauth.core.models.SessionToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SessionTokenRepository
@Autowired constructor(val mapper: SessionTokenMapper) {
    open fun save(sessionToken: SessionToken) {
        mapper.save(sessionToken)
    }

    open fun findAll(): List<SessionToken> {
        return mapper.findAll()
    }

    open fun findByToken(token: String): SessionToken? {
        return mapper.findByToken(token)
    }

    open fun findBySession(sessionId: String): SessionToken? {
        return mapper.findBySession(sessionId)
    }

    open fun delete(sessionToken: SessionToken) {
        return mapper.delete(sessionToken)
    }

    open fun saveOrUpdate(sessionToken: SessionToken) {
        val savedSessionToken = findBySession(sessionToken.sessionId)
        if (savedSessionToken != null) {
            sessionToken.id = savedSessionToken.id
            update(sessionToken)
        } else {
            save(sessionToken)
        }
    }

    open fun update(sessionToken: SessionToken) {
        return mapper.update(sessionToken)
    }
}
