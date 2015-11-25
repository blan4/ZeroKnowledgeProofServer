package com.seniorsigan.qrauth.core.mappers

import com.seniorsigan.qrauth.core.models.LoginRequest
import org.apache.ibatis.annotations.*

interface LoginRequestMapper {
    @Select("""
        SELECT id, session_id, token, expires_at, created_at, updated_at
        FROM login_request
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "session_id", property = "login"),
        Result(column = "token", property = "token"),
        Result(column = "expires_at", property = "expiresAt"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findAll(): List<LoginRequest>

    @Insert("""
        INSERT INTO login_request (session_id, token, expires_at)
        VALUES (#{sessionId}, #{token}, #{expiresAt})
    """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    open fun save(loginRequest: LoginRequest)

    @Select("""
        SELECT id, session_id, token, expires_at, created_at, updated_at
        FROM login_request
        WHERE token = #{token}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "session_id", property = "sessionId"),
        Result(column = "token", property = "token"),
        Result(column = "expires_at", property = "expiresAt"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findByToken(token: String): LoginRequest?

    @Delete("""
        DELETE FROM login_request WHERE id = #{id}
    """)
    open fun delete(loginRequest: LoginRequest)

    @Select("""
        SELECT id, session_id, token, expires_at, created_at, updated_at
        FROM login_request
        WHERE session_id = #{sessionId}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "session_id", property = "sessionId"),
        Result(column = "token", property = "token"),
        Result(column = "expires_at", property = "expiresAt"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findBySession(sessionId: String): LoginRequest?

    @Update("""
        UPDATE login_request
        SET token = #{token}, expires_at = #{expiresAt}
        WHERE id = #{id}
    """)
    open fun update(loginRequest: LoginRequest)
}
