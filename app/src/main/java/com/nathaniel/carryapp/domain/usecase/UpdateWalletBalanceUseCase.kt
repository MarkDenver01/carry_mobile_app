package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.request.UpdateWalletBalanceRequest
import com.nathaniel.carryapp.domain.response.WalletResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

class UpdateWalletBalanceUseCase @Inject constructor(
    private val repo: ApiRepository
) {
    suspend operator fun invoke(request: UpdateWalletBalanceRequest): NetworkResult<WalletResponse> {
        return repo.updateWalletBalance(request)
    }
}