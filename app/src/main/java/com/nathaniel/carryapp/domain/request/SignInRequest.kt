package com.nathaniel.carryapp.domain.request

data class SignInRequest(
    val mailAddress: String,
    val password: String,
)

data class MobileRequest(
    val mobileOrEmail: String,
    val otp: String? = null
)

data class LoginResponse(
    val userId: Long,
    val jwtToken: String?,
    val jwtIssuedAt: String?,
    val jwtExpirationTime: String?,
    val userName: String? = "",
    val role: String? = "",
    val status: Boolean = true,
    val message: String? = null,
    val customerResponse: CustomerResponse?,
    val driverResponse: DriverResponse?
)

data class CustomerResponse(
    val customerId: Long? = 0,
    val userName: String? = "",
    val email: String? = "",
    val mobileNumber: String?,
    val roleState: String? = "",
    val photoUrl: String? = "",
    val address: String? = "",
    val createdDate: String? = "",
    val accountStatus: String? = ""
)

data class DriverResponse(
    val driverId: Long? = 0,
    val userName: String? = "",
    val email: String? = "",
    val mobileNumber: String?,
    val roleState: String? = "",
    val photoUrl: String? = "",
    val address: String? = "",
    val driversLicenseNumber: String? = "",
    val frontIdUrl: String? = "",
    val backIdUrl: String? = "",
    val createdDate: String? = "",
    val accountStatus: String? = ""
)

data class CustomerRegistrationRequest(
    val userName: String,
    val address: String,
    val mobileNumber: String,
    val email: String,
    val photoUrl: String? = null
)