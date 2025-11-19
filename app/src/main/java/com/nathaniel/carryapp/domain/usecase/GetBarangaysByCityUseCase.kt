package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.Barangay
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import jakarta.inject.Inject

sealed class BarangayResult {
    data class Success(val barangays: List<Barangay>) : BarangayResult()
    data class Error(val message: String) : BarangayResult()
}

class GetBarangaysByCityUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(cityCode: String): BarangayResult {
        return when (val result = apiRepository.getBarangaysByCity(cityCode)) {
            is NetworkResult.Success -> {
                val data = result.data ?: return BarangayResult.Error("Empty barangay list")
                BarangayResult.Success(data)
            }

            is NetworkResult.Error -> {
                BarangayResult.Error(result.message ?: "Failed to fetch barangays")
            }

            else -> BarangayResult.Error("Unexpected error")
        }
    }
}
