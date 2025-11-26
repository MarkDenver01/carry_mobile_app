package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import jakarta.inject.Inject

class SaveUserSessionUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke(session: Boolean) {
        localRepository.saveUserSession(session)
    }
}