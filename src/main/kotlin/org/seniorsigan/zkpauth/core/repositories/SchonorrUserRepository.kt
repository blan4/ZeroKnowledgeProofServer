package org.seniorsigan.zkpauth.core.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import org.seniorsigan.zkpauth.core.models.SchonorrSecret
import org.seniorsigan.zkpauth.core.models.SchonorrUser
import org.seniorsigan.zkpauth.core.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class SchonorrUserRepository
@Autowired constructor(
    val userRepository: UserRepository,
    val objectMapper: ObjectMapper
): AnyUserRepository<SchonorrUser> {
    private fun User.toSKeyUser(): SchonorrUser {
        if (this.algorithm != SchonorrUser.algorithm) error("Can't convert user ${this.algorithm} to ${SchonorrUser.algorithm}")
        val secret = objectMapper.readValue(this.secret, SchonorrSecret::class.java)
        return SchonorrUser(id = this.id, login = this.login, secret = secret, createdAt = this.createdAt, updatedAt = this.updatedAt)
    }

    private fun SchonorrUser.toUser(): User {
        val secret = objectMapper.writeValueAsString(secret)
        return User(id = id, algorithm = algorithm, createdAt = createdAt, login = login, secret = secret, updatedAt = updatedAt)
    }

    override fun findAll(): List<SchonorrUser> {
        val users = userRepository.findAllByAlgorithm(SchonorrUser.algorithm)
        return users.map{it.toSKeyUser()}
    }

    override fun save(user: SchonorrUser) {
        userRepository.save(user.toUser())
    }

    override fun update(user: SchonorrUser) {
        userRepository.update(user.toUser())
    }

    override fun find(login: String): SchonorrUser? {
        val user = userRepository.findByAlgorithm(login, SchonorrUser.algorithm)
        return user?.toSKeyUser()
    }
}
