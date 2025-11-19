package com.nathaniel.carryapp.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.nathaniel.carryapp.data.repository.GeocodingRepository
import javax.inject.Inject

sealed class GeocodeResult<out T> {
    data class Success<T>(val data: T) : GeocodeResult<T>()
    data class Error(val message: String) : GeocodeResult<Nothing>()
}

class ForwardGeocodeUseCase @Inject constructor(
    private val repository: GeocodingRepository
) {
    suspend operator fun invoke(address: String): GeocodeResult<LatLng> {
        return try {
            val result = repository.forwardGeocode(address)
            if (result != null) {
                GeocodeResult.Success(result)
            } else {
                GeocodeResult.Error("No location found for address: $address")
            }
        } catch (e: Exception) {
            GeocodeResult.Error(e.message ?: "Unknown error")
        }
    }
}