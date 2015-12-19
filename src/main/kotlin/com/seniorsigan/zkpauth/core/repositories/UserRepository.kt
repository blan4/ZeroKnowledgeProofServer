package com.seniorsigan.zkpauth.core.repositories

import com.seniorsigan.zkpauth.core.mappers.UserMapper
import com.seniorsigan.zkpauth.core.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
open class UserRepository
@Autowired constructor(val mapper: UserMapper){
    open fun findAll(): List<User> {
        return mapper.findAll()
    }

    open fun save(user: User) {
        return mapper.save(user)
    }

    open fun update(user: User) {
        return mapper.update(user)
    }

    open fun find(login: String): User? {
        return mapper.find(login)
    }
}
