
package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

sealed class RecommendationResult {
    data class Success(val products: List<Product>) : RecommendationResult()
    data class Error(val message: String) : RecommendationResult()
}

class GetRecommendationsUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(customerId: Long): RecommendationResult {
        return when (val result = apiRepository.getRecommendations(customerId)) {
            is NetworkResult.Success -> {
                val data = result.data ?: return RecommendationResult.Error("Empty recommendations")
                RecommendationResult.Success(data)
            }
            is NetworkResult.Error -> RecommendationResult.Error(result.message ?: "Failed to load recommendations")
            else -> RecommendationResult.Error("Unexpected error")
        }
    }
}
