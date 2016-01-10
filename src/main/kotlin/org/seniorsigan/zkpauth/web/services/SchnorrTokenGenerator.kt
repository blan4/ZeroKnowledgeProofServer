package org.seniorsigan.zkpauth.web.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.SchnorrUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
open class SchnorrTokenGenerator
@Autowired constructor(
    objectMapper: ObjectMapper
): TokenGenerator(
    objectMapper
) {
    @Value("\${domainName}")
    lateinit override var domainName: String
    override val loginPath: String = "/login/schnorr"
    override val signupPath: String = "/signup/schnorr"
    override val algorithm: String = SchnorrUser.algorithm
}
