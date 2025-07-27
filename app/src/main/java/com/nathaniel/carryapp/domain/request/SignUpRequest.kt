package com.nathaniel.carryapp.domain.request

data class SignUpRequest(
    val userName: String,
    val password: String,
    val address: String,
    val phone: String,
    val mailAddress: String
)