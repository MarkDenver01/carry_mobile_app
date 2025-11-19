package com.nathaniel.carryapp.domain.mapper

import com.nathaniel.carryapp.domain.model.Barangay
import com.nathaniel.carryapp.domain.model.City
import com.nathaniel.carryapp.domain.model.Province
import com.nathaniel.carryapp.domain.response.BarangayResponse
import com.nathaniel.carryapp.domain.response.CityResponse
import com.nathaniel.carryapp.domain.response.ProvinceResponse

object ProvinceMapper {
    fun toDomainList(list: List<ProvinceResponse>): List<Province> =
        list.map {
            Province(
                code = it.code,
                name = it.name,
                regionCode = it.regionCode
            )
        }
}

object CityMapper {
    fun toDomainList(responses: List<CityResponse>): List<City> {
        return responses.map { res ->
            City(
                code = res.code,
                name = res.name
            )
        }
    }
}

object BarangayMapper {
    fun toDomainList(responses: List<BarangayResponse>): List<Barangay> {
        return responses.map { res ->
            Barangay(
                code = res.code,
                name = res.name
            )
        }
    }
}