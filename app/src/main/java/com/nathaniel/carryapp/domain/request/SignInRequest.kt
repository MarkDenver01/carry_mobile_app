package com.nathaniel.carryapp.domain.request

data class SignInRequest (
    val mailAddress: String,
    val password: String,
)

data class MobileRequest(
    val mobileNumber: String,
    val otp: String? = null
)

data class LoginResponse(
    val jwtToken: String?,
    val jwtIssuedAt: String?,
    val jwtExpirationTime: String?,
    val userName: String?,
    val role: String?,
    val status: Boolean = true,
    val message: String? = null,
    // val customerResponse: CustomerResponse?,
    // val driverResponse: DriverResponse?
)