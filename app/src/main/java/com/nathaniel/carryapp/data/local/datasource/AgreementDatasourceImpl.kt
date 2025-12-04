package com.nathaniel.carryapp.data.local.datasource

import com.nathaniel.carryapp.data.local.room.dao.AgreementTermsDao
import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity
import com.nathaniel.carryapp.domain.datasource.AgreementDatasource
import javax.inject.Inject

class AgreementDatasourceImpl @Inject constructor(
    private val agreementTermsDao: AgreementTermsDao
) : AgreementDatasource {
    override suspend fun insertAgreement(agreementTermsEntity: AgreementTermsEntity) {
        agreementTermsDao.insertAgreement(agreementTermsEntity)
    }

    override suspend fun isVerified(email: String): Boolean? {
        return agreementTermsDao.isVerified(email)
    }

    override suspend fun clearAgreementStatus() {
        agreementTermsDao.clearAgreementStatus()
    }
}