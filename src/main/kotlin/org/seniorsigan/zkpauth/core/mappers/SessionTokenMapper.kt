package org.seniorsigan.zkpauth.core.mappers

import org.apache.ibatis.annotations.*
import org.seniorsigan.zkpauth.core.models.SessionToken

interface SessionTokenMapper {
    @Select("""
        SELECT id, session_id, token, meta, expires_at, created_at, updated_at
        FROM session_token
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "session_id", property = "login"),
        Result(column = "token", property = "token"),
        Result(column = "meta", property = "meta"),
        Result(column = "expires_at", property = "expiresAt"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findAll(): List<SessionToken>

    @Insert("""
        INSERT INTO session_token (session_id, token, meta, expires_at)
        VALUES (#{sessionId}, #{token}, #{meta}::jsonb, #{expiresAt})
    """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    open fun save(sessionToken: SessionToken)

    @Select("""
        SELECT id, session_id, token, meta, expires_at, created_at, updated_at
        FROM session_token
        WHERE token = #{token}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "session_id", property = "sessionId"),
        Result(column = "token", property = "token"),
        Result(column = "meta", property = "meta"),
        Result(column = "expires_at", property = "expiresAt"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findByToken(token: String): SessionToken?

    @Delete("""
        DELETE FROM session_token WHERE id = #{id}
    """)
    open fun delete(sessionToken: SessionToken)

    @Select("""
        SELECT id, session_id, token, meta, expires_at, created_at, updated_at
        FROM session_token
        WHERE session_id = #{sessionId}
    """)
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "session_id", property = "sessionId"),
        Result(column = "meta", property = "meta"),
        Result(column = "token", property = "token"),
        Result(column = "expires_at", property = "expiresAt"),
        Result(column = "created_at", property = "createdAt"),
        Result(column = "updated_at", property = "updatedAt")
    )
    open fun findBySession(sessionId: String): SessionToken?

    @Update("""
        UPDATE session_token
        SET token = #{token}, meta = #{meta}::jsonb, expires_at = #{expiresAt}
        WHERE id = #{id}
    """)
    open fun update(sessionToken: SessionToken)
}
