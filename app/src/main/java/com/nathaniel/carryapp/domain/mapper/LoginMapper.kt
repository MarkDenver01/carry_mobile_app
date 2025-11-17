package com.nathaniel.carryapp.domain.mapper

import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.domain.request.LoginResponse

object LoginMapper {

    fun toLoginEntity(data: LoginResponse): LoginEntity =
        LoginEntity(
            userId = data.userId,
            userName = data.userName ?: "",
            jwtToken = data.jwtToken ?: "",
            jwtIssueAt = data.jwtIssuedAt ?: "",
            jwtExpirationTime = data.jwtExpirationTime ?: "",
            role = data.role,
            loginStatus = "accountLoggedIn",
            customerId = data.customerResponse?.customerId,
            driverId = data.driverResponse?.driverId
        )

    fun toCustomerEntity(data: LoginResponse): CustomerEntity? =
        data.customerResponse?.let {
            CustomerEntity(
                customerId = it.customerId ?: -1,
                userId = data.userId,
                userName = it.userName,
                email = it.email,
                mobileNumber = it.mobileNumber,
                roleState = it.roleState,
                photoUrl = it.photoUrl,
                address = it.address,
                createdDate = it.createdDate,
                accountStatus = it.accountStatus
            )
        }

    fun toDriverEntity(data: LoginResponse): DriverEntity? =
        data.driverResponse?.let {
            DriverEntity(
                driverId = it.driverId ?: -1,
                userId = data.userId,
                userName = it.userName,
                email = it.email,
                mobileNumber = it.mobileNumber,
                roleState = it.roleState,
                photoUrl = it.photoUrl,
                address = it.address,
                driverLicenseNumber = it.driversLicenseNumber,
                frontIdUrl = it.frontIdUrl,
                backIdUrl = it.backIdUrl,
                createdDate = it.createdDate,
                accountStatus = it.accountStatus
            )
        }
}
