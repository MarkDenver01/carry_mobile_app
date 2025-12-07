package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.ProductBanner
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

sealed class ProductBannerResult {
    data class Success(val productBanners: List<ProductBanner>) : ProductBannerResult()
    data class Error(val message: String) : ProductBannerResult()
}

class GetAllProductBannerUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(): ProductBannerResult {
        return when (val result = apiRepository.getProductBanner()) {
            is NetworkResult.Success -> {
                val data = result.data ?: return ProductBannerResult.Error("Empty product banner")
                ProductBannerResult.Success(data)
            }

            is NetworkResult.Error -> {
                ProductBannerResult.Error(result.message ?: "Failed to get the banner")
            }

            else -> ProductBannerResult.Error("unexpected error")
        }
    }
}