package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import jakarta.inject.Inject

class SaveMobileOrEmailUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke(mobileOrEmail: String) {
        localRepository.saveMobileOrEmail(mobileOrEmail)
    }
}