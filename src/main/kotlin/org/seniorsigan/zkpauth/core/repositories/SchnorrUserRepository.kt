package org.seniorsigan.zkpauth.core.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.SchnorrSecret
import org.seniorsigan.zkpauth.core.models.SchnorrUser
import org.seniorsigan.zkpauth.core.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SchnorrUserRepository
@Autowired constructor(
    val userRepository: UserRepository,
    val objectMapper: ObjectMapper
): AnyUserRepository<SchnorrUser> {
    private fun User.toSKeyUser(): SchnorrUser {
        if (this.algorithm != SchnorrUser.algorithm) error("Can't convert user ${this.algorithm} to ${SchnorrUser.algorithm}")
        val secret = objectMapper.readValue(this.secret, SchnorrSecret::class.java)
        return SchnorrUser(id = this.id, login = this.login, secret = secret, createdAt = this.createdAt, updatedAt = this.updatedAt)
    }

    private fun SchnorrUser.toUser(): User {
        val secret = objectMapper.writeValueAsString(secret)
        return User(id = id, algorithm = algorithm, createdAt = createdAt, login = login, secret = secret, updatedAt = updatedAt)
    }

    override fun findAll(): List<SchnorrUser> {
        val users = userRepository.findAllByAlgorithm(SchnorrUser.algorithm)
        return users.map{it.toSKeyUser()}
    }

    override fun save(user: SchnorrUser) {
        userRepository.save(user.toUser())
    }

    override fun update(user: SchnorrUser) {
        userRepository.update(user.toUser())
    }

    override fun find(login: String): SchnorrUser? {
        val user = userRepository.findByAlgorithm(login, SchnorrUser.algorithm)
        return user?.toSKeyUser()
    }
}
