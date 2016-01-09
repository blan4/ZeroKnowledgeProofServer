package org.seniorsigan.zkpauth.core.repositories

interface AnyUserRepository<AnyUser> {
    fun findAll(): List<AnyUser>
    fun save(user: AnyUser)
    fun update(user: AnyUser)
    fun find(login: String): AnyUser?
}
