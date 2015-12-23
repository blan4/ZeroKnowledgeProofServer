package org.seniorsigan.zkpauth.core.mappers

import org.apache.ibatis.annotations.*
import org.seniorsigan.zkpauth.core.models.User

interface UserMapper {
    @Select("""
        SELECT id, login, token, tokens_used, created_at, updated_at
        FROM user_login
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "login", property = "login"),
        Result(column = "token", property = "token"),
        Result(column = "tokens_used", property = "tokensUsed"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findAll(): List<User>

    @Select("""
        SELECT id, login, token, tokens_used, created_at, updated_at
        FROM user_login
        WHERE login = #{login}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "login", property = "login"),
        Result(column = "token", property = "token"),
        Result(column = "tokens_used", property = "tokensUsed"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun find(login: String): User?

    @Insert("""
        INSERT INTO user_login (login, token)
        VALUES (#{login}, #{token})
    """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    open fun save(user: User)

    @Update("""
        UPDATE user_login
        SET token = #{token}, tokens_used = #{tokensUsed}
        WHERE id = #{id}
    """)
    open fun update(user: User)
}
