package org.seniorsigan.zkpauth.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.session.ExpiringSession
import org.springframework.stereotype.Service

@Service
class SessionService
@Autowired constructor(
    val template: RedisTemplate<String, ExpiringSession>
) {
    fun bound(sessionId: String, login: String) {
        val bound = template.boundHashOps<String, Any>(getId(sessionId))
        bound.put("sessionAttr:user", login)
    }

    fun getId(id: String): String {
        return "spring:session:sessions:$id"
    }
}
