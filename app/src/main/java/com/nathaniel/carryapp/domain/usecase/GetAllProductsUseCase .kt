package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import jakarta.inject.Inject

sealed class ProductResult {
    data class Success(val products: List<Product>) : ProductResult()
    data class Error(val message: String) : ProductResult()
}

class GetAllProductsUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(): ProductResult {

        return when (val result = apiRepository.getAllProducts()) {

            is NetworkResult.Success -> {
                val data = result.data ?: return ProductResult.Error("Empty product list")
                ProductResult.Success(data)
            }

            is NetworkResult.Error -> {
                ProductResult.Error(result.message ?: "Failed to fetch products")
            }

            else -> ProductResult.Error("Unexpected error")
        }
    }
}