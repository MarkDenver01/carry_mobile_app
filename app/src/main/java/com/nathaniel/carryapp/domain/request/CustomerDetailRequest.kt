package com.nathaniel.carryapp.domain.request

data class CustomerDetailRequest(
    val userName: String,
    val email: String,
    val mobileNumber: String,
    var photoUrl: String,
    val address: String
)