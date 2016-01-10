package org.seniorsigan.zkpauth.core.models

import java.math.BigInteger
import java.sql.Timestamp
import java.util.*

data class SchonorrUser(
    var id: Long = 0L,
    var login: String = "",
    var secret: SchonorrSecret = SchonorrSecret(),
    var createdAt: Timestamp = Timestamp(Date().time),
    var updatedAt: Timestamp = Timestamp(Date().time)
) {
    val algorithm = SchonorrUser.algorithm

    companion object {
        val algorithm = "schonorr"
    }
}

data class SchonorrSecret(
    var p: BigInteger = BigInteger.ZERO,
    var q: BigInteger = BigInteger.ZERO,
    var g: BigInteger = BigInteger.ZERO
)
