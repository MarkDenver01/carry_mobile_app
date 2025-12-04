package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity
import com.nathaniel.carryapp.data.repository.LocalRepository
import javax.inject.Inject

class SaveAgreementUseCase @Inject constructor(
    private val local: LocalRepository
) {
    suspend operator fun invoke(agreementTermsEntity: AgreementTermsEntity) =
        local.saveAgreement(agreementTermsEntity)
}

class CheckAgreementStatusUseCase @Inject constructor(
    private val local: LocalRepository
) {
    suspend operator fun invoke(email: String): Boolean? = local.isVerifiedAgreement(email)
}

class ClearAgreementStatusUseCase @Inject constructor(
    private val local: LocalRepository
) {
    suspend operator fun invoke() = local.clearAgreementStatus()
}