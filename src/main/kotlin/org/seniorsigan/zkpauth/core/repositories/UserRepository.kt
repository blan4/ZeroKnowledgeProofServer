package org.seniorsigan.zkpauth.core.repositories

import org.seniorsigan.zkpauth.core.mappers.UserMapper
import org.seniorsigan.zkpauth.core.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class UserRepository
@Autowired constructor(
    val mapper: UserMapper
) {
    open fun findAllByAlgorithm(algorithm: String): List<User> = mapper.findAllByAlgorithm(algorithm)
    open fun findByAlgorithm(login: String, algorithm: String): User? = mapper.findByAlgorithm(login, algorithm)
    open fun findAll(): List<User> = mapper.findAll()
    open fun save(user: User) = mapper.save(user)
    open fun update(user: User) = mapper.update(user)
    open fun find(login: String): User? = mapper.find(login)
}
