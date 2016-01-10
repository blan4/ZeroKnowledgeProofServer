package org.seniorsigan.zkpauth.core.services

import org.seniorsigan.zkpauth.core.models.SchnorrSecret
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class SchnorrService {
    fun isValidPublicKey(key: SchnorrSecret): Boolean {
        return with(key) {
            g.modPow(q, p) == BigInteger.ONE
        }
    }
}
