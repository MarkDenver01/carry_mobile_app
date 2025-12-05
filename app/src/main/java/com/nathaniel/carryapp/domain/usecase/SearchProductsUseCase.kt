package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repo: ApiRepository
) {
    suspend operator fun invoke(query: String): ProductResult {
        return when (val res = repo.searchProducts(query)) {
            is NetworkResult.Success ->
                ProductResult.Success(res.data ?: emptyList())

            is NetworkResult.Error ->
                ProductResult.Error(res.message ?: "Search error")

            else -> ProductResult.Error("Unexpected search error")
        }
    }
}
