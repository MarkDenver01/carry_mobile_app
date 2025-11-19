package com.nathaniel.carryapp.domain.request

data class CustomerDetailRequest(
    val userName: String,
    val email: String,
    val mobileNumber: String,
    val photoUrl: String,
    val address: String
)