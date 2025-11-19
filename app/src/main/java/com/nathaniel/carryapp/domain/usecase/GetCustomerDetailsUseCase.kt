package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import jakarta.inject.Inject

class GetCustomerDetailsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(): CustomerDetailRequest? {
        return localRepository.getCustomerDetails()
    }
}