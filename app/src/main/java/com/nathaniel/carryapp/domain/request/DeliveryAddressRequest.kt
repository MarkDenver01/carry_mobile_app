package com.nathaniel.carryapp.domain.request

import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity

data class DeliveryAddressRequest(
    val provinceCode: String,
    val provinceName: String,
    val cityCode: String,
    val cityName: String,
    val barangayCode: String,
    val barangayName: String,
    val addressDetails: String,
    val landMark: String
)

object DeliveryAddressMapper {
    // ─────────────────────────────────────────────
    // REQUEST → ENTITY (for saving to Room)
    // ─────────────────────────────────────────────
    fun toEntity(request: DeliveryAddressRequest): DeliveryAddressEntity {
        return DeliveryAddressEntity(
            provinceCode = request.provinceCode,
            provinceName = request.provinceName,

            cityCode = request.cityCode,
            cityName = request.cityName,

            barangayCode = request.barangayCode,
            barangayName = request.barangayName,

            addressDetail = request.addressDetails,
            landmark = request.landMark
        )
    }


    // ─────────────────────────────────────────────
    // ENTITY → REQUEST (optional, useful for restore)
    // ─────────────────────────────────────────────
    fun toRequest(entity: DeliveryAddressEntity): DeliveryAddressRequest {
        return DeliveryAddressRequest(
            provinceCode = entity.provinceCode,
            provinceName = entity.provinceName,

            cityCode = entity.cityCode,
            cityName = entity.cityName,

            barangayCode = entity.barangayCode,
            barangayName = entity.barangayName,

            addressDetails = entity.addressDetail,
            landMark = entity.landmark ?: ""
        )
    }
}

