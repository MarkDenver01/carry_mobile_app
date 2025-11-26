package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.request.CheckoutRequest
import com.nathaniel.carryapp.domain.response.OrderResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(request: CheckoutRequest): NetworkResult<OrderResponse> {
        return repository.checkOut(request)
    }
}