package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.local.room.entity.LoginSessionEntity
import com.nathaniel.carryapp.data.repository.LocalRepository
import javax.inject.Inject

class SaveLoginSessionUseCase @Inject constructor(
    private val local: LocalRepository
) {
    suspend operator fun invoke(loginSessionEntity: LoginSessionEntity) =
        local.saveLoginSession(loginSessionEntity)
}

class CheckLoginSessionUseCase @Inject constructor(
    private val local: LocalRepository
) {
    suspend operator fun invoke(email: String): Boolean? =
        local.isLoggedIn(email)
}

class DeleteLoginSessionUseCase @Inject constructor(
    private val local: LocalRepository
) {
    suspend operator fun invoke() = local.deleteLoginSession()
}
