package com.nathaniel.carryapp.domain.model

data class Province(
    val code: String,
    val name: String,
    val regionCode: String
)

data class City(
    val code: String,
    val name: String
)

data class Barangay(
    val code: String,
    val name: String
)