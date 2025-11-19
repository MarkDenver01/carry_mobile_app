package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.data.local.room.relations.LoginWithCustomer
import com.nathaniel.carryapp.data.local.room.relations.LoginWithDriver
import com.nathaniel.carryapp.domain.request.CustomerRequest

interface LoginLocalDataSource {
    suspend fun saveLogin(
        login: LoginEntity,
        customer: CustomerEntity?,
        driver: DriverEntity?
    )

    suspend fun saveCustomerDetails(
        customerRequest: CustomerRequest?
    )

    suspend fun getCustomerDetails(): CustomerDetailsEntity?

    suspend fun getCurrentLogin(): LoginEntity?

    suspend fun getCustomerSession(): LoginWithCustomer?

    suspend fun getDriverSession(): LoginWithDriver?

    suspend fun logout()
}