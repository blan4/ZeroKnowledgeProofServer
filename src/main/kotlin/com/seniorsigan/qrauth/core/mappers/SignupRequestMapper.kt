package com.seniorsigan.qrauth.core.mappers

import com.seniorsigan.qrauth.core.models.SignupRequest
import org.apache.ibatis.annotations.*

interface SignupRequestMapper {
    @Select("""
        SELECT id, session_id, token, expires_at, created_at, updated_at
        FROM signup_request
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "session_id", property = "signup"),
        Result(column = "token", property = "token"),
        Result(column = "expires_at", property = "expiresAt"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findAll(): List<SignupRequest>

    @Insert("""
        INSERT INTO signup_request (session_id, token, expires_at)
        VALUES (#{sessionId}, #{token}, #{expiresAt})
    """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    open fun save(signupRequest: SignupRequest)

    @Select("""
        SELECT id, session_id, token, expires_at, created_at, updated_at
        FROM signup_request
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
    open fun findByToken(token: String): SignupRequest?

    @Delete("""
        DELETE FROM signup_request WHERE id = #{id}
    """)
    open fun delete(signupRequest: SignupRequest)

    @Select("""
        SELECT id, session_id, token, expires_at, created_at, updated_at
        FROM signup_request
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
    open fun findBySession(sessionId: String): SignupRequest?

    @Update("""
        UPDATE signup_request
        SET token = #{token}, expires_at = #{expiresAt}
        WHERE id = #{id}
    """)
    open fun update(signupRequest: SignupRequest)
}
