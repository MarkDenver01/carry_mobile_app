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

data class CityResponse(
    val code: String,
    val name: String,
    val regionCode: String?,
    val provinceCode: String?
)

data class BarangayResponse(
    val code: String,
    val name: String,
    val cityCode: String?,
    val provinceCode: String?,
    val regionCode: String?
)