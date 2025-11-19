package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import jakarta.inject.Inject

class GetMobileOrEmailUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(): String? {
        return localRepository.getMobileOrEmail()
    }
}