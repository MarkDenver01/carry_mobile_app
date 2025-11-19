package com.nathaniel.carryapp.domain.response

data class CustomerDetailResponse(
    val customerId: Long,
    val userName: String,
    val email: String?,
    val mobileNumber: String?,
    val photoUrl: String?,
    val address: String?,
    val createdDate: String?,
    val accountStatus: String?
)