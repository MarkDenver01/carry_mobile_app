package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.City
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import jakarta.inject.Inject

sealed class CityResult {
    data class Success(val cities: List<City>) : CityResult()
    data class Error(val message: String) : CityResult()
}

class GetCitiesByProvinceUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(provinceCode: String): CityResult {
        return when (val result = apiRepository.getCitiesByProvince(provinceCode)) {
            is NetworkResult.Success -> {
                val data = result.data ?: return CityResult.Error("Empty city list")
                CityResult.Success(data)
            }

            is NetworkResult.Error -> {
                CityResult.Error(result.message ?: "Failed to fetch cities")
            }

            else -> CityResult.Error("Unexpected error")
        }
    }
}
