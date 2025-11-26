package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.response.WalletResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

sealed class GetWalletBalanceResult {
    data class Success(val walletResponse: WalletResponse) : GetWalletBalanceResult()
    data class Failed(val message: String) : GetWalletBalanceResult()
}

class GetWalletBalanceUseCase @Inject constructor(
    private val repo: ApiRepository
) {
    suspend operator fun invoke(mobileNumber: String): GetWalletBalanceResult {
        return when (val result = repo.getWalletBalance(mobileNumber)) {
            is NetworkResult.Success -> GetWalletBalanceResult.Success(result.data!!)
            is NetworkResult.Error -> GetWalletBalanceResult.Failed(result.message ?: "Failed")
            else -> GetWalletBalanceResult.Failed("Unexpected error")
        }
    }
}