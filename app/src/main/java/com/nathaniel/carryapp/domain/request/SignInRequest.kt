package com.nathaniel.carryapp.domain.request

data class SignInRequest (
    val mailAddress: String,
    val password: String,
)