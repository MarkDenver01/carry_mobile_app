package com.nathaniel.carryapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.getValue

data class GeocodedAddress(
    val fullAddressLine: String?,
    val province: String?,
    val city: String?,
    val barangay: String?
)

class GeocodingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    suspend fun forwardGeocode(fullAddress: String): LatLng? = withContext(Dispatchers.IO) {
        try {
            val results = geocoder.getFromLocationName(fullAddress, 1)
            val addr = results?.firstOrNull() ?: return@withContext null
            LatLng(addr.latitude, addr.longitude)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /** LatLng -> structured address */
    suspend fun reverseGeocode(lat: Double, lng: Double): GeocodedAddress? =
        withContext(Dispatchers.IO) {
            try {
                val results = geocoder.getFromLocation(lat, lng, 1)
                val addr = results?.firstOrNull() ?: return@withContext null

                GeocodedAddress(
                    fullAddressLine = addr.getAddressLine(0),
                    province = addr.adminArea,          // Province
                    city = addr.locality ?: addr.subAdminArea,
                    barangay = addr.subLocality,        // Often barangay
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        return suspendCancellableCoroutine { cont ->

            val fused = LocationServices.getFusedLocationProviderClient(context)

            val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

            fused.getCurrentLocation(priority, null)
                .addOnSuccessListener { location ->
                    cont.resume(location)
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }


}