package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.response.UserHistoryResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class GetUserHistoryResult {
    data class Success(val history: List<UserHistoryResponse>) : GetUserHistoryResult()
    data class Error(val message: String) : GetUserHistoryResult()
}

class GetUserHistoryUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(customerId: Long): GetUserHistoryResult =
        withContext(Dispatchers.IO) {
            when (val result = apiRepository.getUserHistory(customerId)) {
                is NetworkResult.Success -> {
                    val history = result.data ?: emptyList()
                    GetUserHistoryResult.Success(history)
                }
                is NetworkResult.Error -> GetUserHistoryResult.Error(result.message ?: "Failed to fetch history")
                else -> GetUserHistoryResult.Error("Unexpected error")
            }
        }
}