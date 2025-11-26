package com.nathaniel.carryapp.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.response.UserHistoryResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class SaveHistoryResult {
    object Success : SaveHistoryResult()
    data class Error(val message: String) : SaveHistoryResult()
}

class SaveUserHistoryUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(customerId: Long, keyword: String): SaveHistoryResult =
        withContext(Dispatchers.IO) {
            when (val result = apiRepository.saveUserHistory(customerId, keyword)) {
                is NetworkResult.Success -> SaveHistoryResult.Success
                is NetworkResult.Error -> SaveHistoryResult.Error(result.message ?: "Unknown error")
                else -> SaveHistoryResult.Error("Unexpected error")
            }
        }
}