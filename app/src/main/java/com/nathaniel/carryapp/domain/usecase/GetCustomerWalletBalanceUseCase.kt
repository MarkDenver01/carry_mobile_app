package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.response.WalletResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

class GetCustomerWalletBalanceUseCase @Inject constructor(
    private val repo: ApiRepository
) {
    suspend operator fun invoke(mobileNumber: String): NetworkResult<WalletResponse> {
        return repo.getCustomerWalletBalance(mobileNumber)
    }
}