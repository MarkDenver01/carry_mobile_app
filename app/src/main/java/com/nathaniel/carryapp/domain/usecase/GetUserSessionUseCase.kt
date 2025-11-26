package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import jakarta.inject.Inject

class GetUserSessionUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke(): Boolean {
        return localRepository.getUserSession()
    }
}