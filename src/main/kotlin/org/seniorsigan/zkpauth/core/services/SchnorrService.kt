package org.seniorsigan.zkpauth.core.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.SchnorrSecret
import org.seniorsigan.zkpauth.core.models.SchnorrUser
import org.seniorsigan.zkpauth.lib.SchnorrSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
open class SchnorrService
@Autowired constructor(
    val sessionTokenService: SessionTokenService,
    val objectMapper: ObjectMapper,
    val schnorrSignature: SchnorrSignature,
    val sessionService: SessionService
) {
    open fun isValidPublicKey(key: SchnorrSecret): Boolean {
        return with(key) {
            g.modPow(q, p) == BigInteger.ONE
        }
    }

    @Throws(ServiceException::class)
    open fun invoke(user: SchnorrUser, token: String, key: String): String {
        println("Start Schnorr authentication flow")
        val st = sessionTokenService.find(token)
        try {
            if (st.meta == null) {
                println("Schnorr initial state with x: $key")
                val x = BigInteger(key)
                val nonce = schnorrSignature.generateNonce()
                st.meta = objectMapper.writeValueAsString(SchnorrMeta(x, nonce))
                sessionTokenService.update(st)
                return nonce.toString()
            } else {
                println("Schnorr final state with s: $key")
                val s = BigInteger(key)
                val meta = objectMapper.readValue(st.meta, SchnorrMeta::class.java)
                val public = SchnorrSignature.SchnorrPublicKey(
                    user.secret.p,
                    user.secret.q,
                    user.secret.g,
                    user.secret.y
                )
                if (schnorrSignature.verify(meta.x, s, meta.nonce, public)) {
                    val sessionId = sessionTokenService.use(st.token)
                    sessionService.bound(sessionId, user.login)
                    return "User ${user.login} successfully logged in with algorithm ${user.algorithm}"
                }
            }
        } catch(e: Exception) {
            sessionTokenService.delete(st)
            throw ServiceException("Error during Schnorr signature flow: ${e.message}", e)
        }
        sessionTokenService.delete(st)
        throw ServiceException("Schnorr verification failed")
    }

    data class SchnorrMeta(
        val x: BigInteger = BigInteger.ZERO,
        val nonce: BigInteger = BigInteger.ZERO,
        val s: BigInteger = BigInteger.ZERO
    )
}
