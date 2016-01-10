package org.seniorsigan.zkpauth.web.models

import org.seniorsigan.zkpauth.core.models.SchnorrSecret
import java.math.BigInteger

data class SignupForm(
    var login: String = "",
    var key: String = "",
    var token: String = ""
)

data class SchonorrSignupForm(
    var login: String = "",
    var key: SchnorrSecret = SchnorrSecret(),
    var token: String = ""
) {
    fun valid(): Boolean {
        return key.q != BigInteger.ZERO
            && key.p != BigInteger.ZERO
            && key.g != BigInteger.ZERO
            && login.isNotBlank()
            && token.isNotBlank()
    }
}
