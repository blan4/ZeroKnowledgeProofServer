package com.seniorsigan.qrauth.web.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.session.ExpiringSession
import org.springframework.stereotype.Service

@Service
class SessionService
@Autowired constructor(
    val template: RedisTemplate<String, ExpiringSession>
) {
    fun show(id: String) {
        val bound = template.boundHashOps<String, Any>(getId(id))
        val entries = bound.entries()
        for ((k, v) in entries) {
            println("$k: $v")
        }
        bound.put("sessionAttr:user", "blan4")
    }

    fun getId(id: String): String {
        return "spring:session:sessions:$id"
    }
}
