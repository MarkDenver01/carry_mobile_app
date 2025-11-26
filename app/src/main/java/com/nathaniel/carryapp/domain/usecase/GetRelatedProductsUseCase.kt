package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.usecase.RelatedProductsResult.*
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

class GetRelatedProductsUseCase @Inject constructor(
    private val repo: ApiRepository
) {
    suspend operator fun invoke(productId: Long): RelatedProductsResult {
        return when (val res = repo.getRelatedProducts(productId)) {
            is NetworkResult.Success -> Success(res.data)
            is NetworkResult.Error -> Error(res.message)
            is NetworkResult.Idle -> TODO()
            is NetworkResult.Loading -> TODO()
        }
    }
}

sealed class RelatedProductsResult {
    data class Success(val products: List<Product>?) : RelatedProductsResult()
    data class Error(val message: String?) : RelatedProductsResult()
}