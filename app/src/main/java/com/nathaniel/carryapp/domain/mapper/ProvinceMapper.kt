package com.nathaniel.carryapp.domain.mapper

import com.nathaniel.carryapp.domain.model.Province
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