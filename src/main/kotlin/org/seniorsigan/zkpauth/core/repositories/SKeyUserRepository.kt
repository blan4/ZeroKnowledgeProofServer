package org.seniorsigan.zkpauth.core.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.SKeySecret
import org.seniorsigan.zkpauth.core.models.SKeyUser
import org.seniorsigan.zkpauth.core.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SKeyUserRepository
@Autowired constructor(
    val userRepository: UserRepository,
    val objectMapper: ObjectMapper
): AnyUserRepository<SKeyUser> {
    private fun User.toSKeyUser(): SKeyUser {
        if (this.algorithm != SKeyUser.algorithm) error("Can't convert user ${this.algorithm} to ${SKeyUser.algorithm}")
        val secret = objectMapper.readValue(this.secret, SKeySecret::class.java)
        return SKeyUser(id = this.id, login = this.login, secret = secret, createdAt = this.createdAt, updatedAt = this.updatedAt)
    }

    private fun SKeyUser.toUser(): User {
        val secret = objectMapper.writeValueAsString(secret)
        return User(id = id, algorithm = algorithm, createdAt = createdAt, login = login, secret = secret, updatedAt = updatedAt)
    }

    override fun findAll(): List<SKeyUser> {
        val users = userRepository.findAllByAlgorithm(SKeyUser.algorithm)
        return users.map{it.toSKeyUser()}
    }

    override fun save(user: SKeyUser) {
        userRepository.save(user.toUser())
    }

    override fun update(user: SKeyUser) {
        userRepository.update(user.toUser())
    }

    override fun find(login: String): SKeyUser? {
        val user = userRepository.findByAlgorithm(login, SKeyUser.algorithm)
        return user?.toSKeyUser()
    }
}
