package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.request.CashInRequest
import com.nathaniel.carryapp.domain.response.CashInInitResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

sealed class CashInResult {
    data class Success(val cashInResponse: CashInInitResponse) : CashInResult()
    data class Failed(val message: String) : CashInResult()
}

class CashInUseCase @Inject constructor(
    private val repo: ApiRepository
) {
    suspend operator fun invoke(req: CashInRequest): CashInResult {
        return when (val result = repo.cashIn(req)) {
            is NetworkResult.Success -> CashInResult.Success(result.data!!)
            is NetworkResult.Error -> CashInResult.Failed(result.message ?: "Cash-in error")
            else -> CashInResult.Failed("Unexpected error")
        }
    }
}