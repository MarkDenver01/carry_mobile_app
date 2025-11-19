package com.nathaniel.carryapp.domain.response

data class ProvinceResponse(
    val code: String,
    val name: String,
    val oldName: String,
    val classification: String,
    val regionCode: String,
    val islandGroupCode: String,
    val population: Int
)