package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import jakarta.inject.Inject

class SaveCustomerDetailsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(customerDetailRequest: CustomerDetailRequest) {
        localRepository.saveCustomerDetails(customerDetailRequest)
    }
}