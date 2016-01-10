package org.seniorsigan.zkpauth.core.services

import org.seniorsigan.zkpauth.core.models.SchonorrSecret
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class SchonorrService {
    fun isValidPublicKey(key: SchonorrSecret): Boolean {
        return with(key) {
            g.modPow(q, p) == BigInteger.ONE
        }
    }
}
