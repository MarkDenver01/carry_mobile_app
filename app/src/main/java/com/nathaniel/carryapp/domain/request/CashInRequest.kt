package com.nathaniel.carryapp.domain.request

data class CashInRequest(
    val mobileNumber: String,
    val amount: Long,
    val email: String
)