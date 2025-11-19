package com.nathaniel.carryapp.domain.mapper

import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.domain.request.CustomerRegistrationRequest
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest

object CustomerDetailsMapper {

    /** Convert Request → Entity (for saving to Room) */
    fun toEntity(request: CustomerDetailRequest): CustomerDetailsEntity {
        return CustomerDetailsEntity(
            customerId = 1, // fixed ID since it's a single customer profile
            userName = request.userName,
            email = request.email,
            mobileNumber = request.mobileNumber,
            photoUrl = request.photoUrl,
            address = request.address
        )
    }

    /** Convert Entity → Request (helpful when retrieving later) */
    fun fromEntity(entity: CustomerDetailsEntity): CustomerDetailRequest {
        return CustomerDetailRequest(
            userName = entity.userName.orEmpty(),
            email = entity.email.orEmpty(),
            mobileNumber = entity.mobileNumber.orEmpty(),
            photoUrl = entity.photoUrl.orEmpty(),
            address = entity.address.orEmpty()
        )
    }

    /* ---------------------------------------------
    Convert RegistrationRequest → Core Request
    (Used before saving to server or Room)
 --------------------------------------------- */
    fun toCustomerRequest(reg: CustomerRegistrationRequest): CustomerDetailRequest {
        return CustomerDetailRequest(
            userName = reg.userName,
            email = reg.email,
            mobileNumber = reg.mobileNumber,
            photoUrl = reg.photoUrl ?: "",
            address = reg.address
        )
    }
}