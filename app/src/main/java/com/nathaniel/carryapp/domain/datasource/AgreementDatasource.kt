package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity

interface AgreementDatasource {
    suspend fun insertAgreement(agreementTermsEntity: AgreementTermsEntity)

    suspend fun isVerified(email: String): Boolean?

    suspend fun clearAgreementStatus()
}