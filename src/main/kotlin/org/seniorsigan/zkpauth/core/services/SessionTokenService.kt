package org.seniorsigan.zkpauth.core.services

import org.seniorsigan.zkpauth.core.models.RequestInfo
import org.seniorsigan.zkpauth.core.models.SessionToken
import org.seniorsigan.zkpauth.core.repositories.SessionTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class SessionTokenService
@Autowired constructor(
    val sessionTokenRepository: SessionTokenRepository
){
    @Transactional(rollbackFor = arrayOf(Exception::class))
    open fun createToken(sessionId: String, requestInfo: RequestInfo): SessionToken {
        var uuid: String
        do {
            uuid = UUID.randomUUID().toString()
        } while (sessionTokenRepository.findByToken(uuid) != null)
        val token = SessionToken(sessionId = sessionId, token = uuid, expiresAt = Date(Date().time + 1000 * 60), requestInfo = requestInfo)
        sessionTokenRepository.saveOrUpdate(token)
        return token
    }

    @Transactional(noRollbackFor = arrayOf(ServiceException::class))
    @Throws(ServiceException::class)
    open fun use(token: String): String {
        val st = sessionTokenRepository.findByToken(token) ?: throw ServiceException("Can't find session token $token")
        sessionTokenRepository.delete(st)
        if (st.expiresAt <= Date()) {
            throw ServiceException("Session token $token expired")
        }
        return st.sessionId
    }

    @Transactional(noRollbackFor = arrayOf(ServiceException::class))
    @Throws(ServiceException::class)
    open fun find(token: String): SessionToken {
        val st = sessionTokenRepository.findByToken(token) ?: throw ServiceException("Can't find session token $token")
        if (st.expiresAt <= Date()) {
            sessionTokenRepository.delete(st)
            throw ServiceException("Session token $token expired")
        }
        return st
    }

    open fun update(st: SessionToken) {
        sessionTokenRepository.update(st)
    }

    open fun delete(st: SessionToken) {
        sessionTokenRepository.delete(st)
    }
}
