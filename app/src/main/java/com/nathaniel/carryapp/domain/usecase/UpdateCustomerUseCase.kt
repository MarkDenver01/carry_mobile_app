package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import javax.inject.Inject

class UpdateCustomerUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(
        identifier: String,
        request: CustomerDetailRequest
    ) = repository.updateCustomer(identifier, request)
}