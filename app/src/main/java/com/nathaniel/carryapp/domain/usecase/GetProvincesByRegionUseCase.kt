package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Province
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import jakarta.inject.Inject

sealed class ProvinceResult {
    data class Success(val provinces: List<Province>) : ProvinceResult()
    data class Error(val message: String) : ProvinceResult()
}

class GetProvincesByRegionUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {

    suspend operator fun invoke(regionCode: String): ProvinceResult {
        return when (val result = apiRepository.getProvincesByRegion(regionCode)) {

            is NetworkResult.Success -> {
                val data = result.data ?: return ProvinceResult.Error("Empty province list")
                ProvinceResult.Success(data)
            }

            is NetworkResult.Error -> {
                ProvinceResult.Error(result.message ?: "Failed to fetch provinces")
            }

            else -> ProvinceResult.Error("Unexpected error")
        }
    }
}