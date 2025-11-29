package com.nathaniel.carryapp.domain.request

data class DriverLocationUpdateRequest(
    val driverId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float?,
    val timestamp: Long
)