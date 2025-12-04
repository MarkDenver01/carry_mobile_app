package com.nathaniel.carryapp.domain.model

import com.nathaniel.carryapp.data.local.room.entity.LoginSessionEntity

data class LoginSessionRequest(
    val email: String,
    val session: Boolean
)

data class LoginSessionResponse(
    val session: Boolean
)

object LoginSessionMapper {
    // ─────────────────────────────────────────────
    // REQUEST → ENTITY (for saving to Room)
    // ─────────────────────────────────────────────
    fun toEntity(request: LoginSessionRequest): LoginSessionEntity {
        return LoginSessionEntity(
            email = request.email,
            session = request.session
        )
    }

    // ─────────────────────────────────────────────
    // ENTITY → REQUEST (optional, useful for restore)
    // ─────────────────────────────────────────────
    fun toResponse(entity: LoginSessionEntity): LoginSessionResponse {
        return LoginSessionResponse(session = entity.session)
    }
}