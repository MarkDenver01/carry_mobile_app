package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.response.ProductCategoryResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

sealed class CategoryResult {
    data class Success(val data: ProductCategoryResponse?) : CategoryResult()
    data class Error(val message: String) : CategoryResult()
    object Loading : CategoryResult()
}

class GetAllCategoryUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(): CategoryResult {

        return when (val result = repository.getAllCategory()) {

            is NetworkResult.Success ->
                CategoryResult.Success(result.data)

            is NetworkResult.Error ->
                CategoryResult.Error(result.message ?: "Unknown error")

            is NetworkResult.Loading ->
                CategoryResult.Loading

            is NetworkResult.Idle ->
                CategoryResult.Loading
        }
    }
}