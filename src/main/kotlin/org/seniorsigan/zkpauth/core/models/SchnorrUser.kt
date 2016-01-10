package org.seniorsigan.zkpauth.core.models

import java.math.BigInteger
import java.sql.Timestamp
import java.util.*

data class SchnorrUser(
    var id: Long = 0L,
    var login: String = "",
    var secret: SchnorrSecret = SchnorrSecret(),
    var createdAt: Timestamp = Timestamp(Date().time),
    var updatedAt: Timestamp = Timestamp(Date().time)
) {
    val algorithm = SchnorrUser.algorithm

    companion object {
        val algorithm = "schnorr"
    }
}

data class SchnorrSecret(
    var p: BigInteger = BigInteger.ZERO,
    var q: BigInteger = BigInteger.ZERO,
    var g: BigInteger = BigInteger.ZERO,
    var y: BigInteger = BigInteger.ZERO
)
