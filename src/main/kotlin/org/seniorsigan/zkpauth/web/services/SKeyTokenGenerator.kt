package org.seniorsigan.zkpauth.web.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.SKeyUser
import org.seniorsigan.zkpauth.core.repositories.LoginRequestRepository
import org.seniorsigan.zkpauth.core.repositories.SignupRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SKeyTokenGenerator
@Autowired constructor(
    loginRequestRepository: LoginRequestRepository,
    signupRequestRepository: SignupRequestRepository,
    objectMapper: ObjectMapper
): TokenGenerator(
    loginRequestRepository,
    signupRequestRepository,
    objectMapper
) {
    @Value("\${domainName}")
    lateinit override var domainName: String
    override val loginPath: String = "/login/skey"
    override val signupPath: String = "/signup/skey"
    override val algorithm: String = SKeyUser.algorithm
}