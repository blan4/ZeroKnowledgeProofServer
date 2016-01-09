package org.seniorsigan.zkpauth.core.mappers

import org.apache.ibatis.annotations.*
import org.seniorsigan.zkpauth.core.models.User

interface UserMapper {
    @Select("""
        SELECT id, login, algorithm, secret, created_at, updated_at
        FROM user_login
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "login", property = "login"),
        Result(column = "secret", property = "secret"),
        Result(column = "algorithm", property = "algorithm"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findAll(): List<User>

    @Select("""
        SELECT id, login, algorithm, secret, created_at, updated_at
        FROM user_login
        WHERE login = #{login}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "login", property = "login"),
        Result(column = "secret", property = "secret"),
        Result(column = "algorithm", property = "algorithm"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun find(login: String): User?

    @Insert("""
        INSERT INTO user_login (login, algorithm, secret)
        VALUES (#{login}, #{algorithm}, #{secret})
    """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    open fun save(user: User)

    @Update("""
        UPDATE user_login
        SET secret = #{secret}
        WHERE id = #{id}
    """)
    open fun update(user: User)

    @Select("""
        SELECT id, login, algorithm, secret, created_at, updated_at
        FROM user_login
        WHERE algorithm = #{algorithm}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "login", property = "login"),
        Result(column = "secret", property = "secret"),
        Result(column = "algorithm", property = "algorithm"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findAllByAlgorithm(@Param("algorithm") algorithm: String): List<User>

    @Select("""
        SELECT id, login, algorithm, secret, created_at, updated_at
        FROM user_login
        WHERE login = #{login} AND algorithm = #{algorithm}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "login", property = "login"),
        Result(column = "secret", property = "secret"),
        Result(column = "algorithm", property = "algorithm"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findByAlgorithm(@Param("login") login: String, @Param("algorithm") algorithm: String): User?
}
