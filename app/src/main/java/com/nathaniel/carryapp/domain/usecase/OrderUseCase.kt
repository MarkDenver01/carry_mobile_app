package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.response.CustomerOrderResponse
import com.nathaniel.carryapp.domain.response.OrderResponse
import javax.inject.Inject

class GetMyOrdersUseCase @Inject constructor(
    private val repository: ApiRepository
) {

    suspend operator fun invoke(customerId: Long): List<CustomerOrderResponse> {
        return repository.getCustomerOrders(customerId)
    }
}