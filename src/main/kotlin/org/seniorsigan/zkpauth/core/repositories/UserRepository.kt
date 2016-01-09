package org.seniorsigan.zkpauth.core.repositories

import org.seniorsigan.zkpauth.core.mappers.UserMapper
import org.seniorsigan.zkpauth.core.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class UserRepository
@Autowired constructor(
    val mapper: UserMapper
): AnyUserRepository<User> {
    internal fun findAllByAlgorithm(algorithm: String): List<User> = mapper.findAllByAlgorithm(algorithm)
    internal fun findByAlgorithm(login: String, algorithm: String): User? = mapper.findByAlgorithm(login, algorithm)

    override fun findAll(): List<User> = mapper.findAll()

    override fun save(user: User) = mapper.save(user)

    override fun update(user: User) = mapper.update(user)

    override fun find(login: String): User? = mapper.find(login)
}
