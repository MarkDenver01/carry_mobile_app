package com.nathaniel.carryapp.domain.model

import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity

data class AgreementRequest(
    val email: String,
    val agreementStatus: Boolean
)

data class AgreementResponse(
    val agreementStatus: Boolean
)

object AgreementMapper {
    // ─────────────────────────────────────────────
    // REQUEST → ENTITY (for saving to Room)
    // ─────────────────────────────────────────────

    fun toEntity(request: AgreementRequest): AgreementTermsEntity {
        return AgreementTermsEntity(
            email = request.email,
            agreement = request.agreementStatus
        )
    }

    // ─────────────────────────────────────────────
    // ENTITY → REQUEST (optional, useful for restore)
    // ─────────────────────────────────────────────
    fun toResponse(entity: AgreementTermsEntity): AgreementResponse {
        return AgreementResponse(
            agreementStatus = entity.agreement
        )
    }
}