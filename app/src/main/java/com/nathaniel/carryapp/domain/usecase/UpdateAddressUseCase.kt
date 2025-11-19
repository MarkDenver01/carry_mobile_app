package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import jakarta.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: LocalRepository
) {

    suspend operator fun invoke(
        provinceName: String?,
        cityName: String?,
        barangayName: String?,
        addressDetail: String?
    ) {
        repository.updateAddress(
            provinceName ?: "",
            cityName ?: "",
            barangayName ?: "",
            addressDetail ?: ""
        )
    }
}