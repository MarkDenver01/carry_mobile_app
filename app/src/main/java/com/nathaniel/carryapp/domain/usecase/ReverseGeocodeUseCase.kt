package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.GeocodedAddress
import com.nathaniel.carryapp.data.repository.GeocodingRepository
import jakarta.inject.Inject

class ReverseGeocodeUseCase @Inject constructor(
    private val repository: GeocodingRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double): GeocodeResult<GeocodedAddress> {
        return try {
            val result = repository.reverseGeocode(lat, lng)
            if (result != null) {
                GeocodeResult.Success(result)
            } else {
                GeocodeResult.Error("Unable to reverse-geocode coordinates ($lat, $lng)")
            }
        } catch (e: Exception) {
            GeocodeResult.Error(e.message ?: "Unknown error")
        }
    }
}