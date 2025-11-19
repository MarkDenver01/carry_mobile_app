package com.nathaniel.carryapp.domain.usecase

import android.location.Location
import com.nathaniel.carryapp.data.repository.GeocodingRepository


class GetCurrentLocationUseCase(
    private val locationRepository: GeocodingRepository
) {
    suspend operator fun invoke(): Location? {
        return locationRepository.getCurrentLocation()
    }
}