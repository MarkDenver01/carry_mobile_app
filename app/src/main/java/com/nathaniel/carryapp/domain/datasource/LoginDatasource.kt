package com.nathaniel.carryapp.domain.datasource

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginSessionEntity
import com.nathaniel.carryapp.data.local.room.relations.LoginWithCustomer
import com.nathaniel.carryapp.data.local.room.relations.LoginWithDriver
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest

interface LoginDatasource {
    suspend fun saveLoginSession(entity: LoginSessionEntity)

    suspend fun isLoggedIn(email: String): Boolean?

    suspend fun deleteLoginSession()

    suspend fun saveLogin(
        login: LoginEntity,
        customer: CustomerEntity?,
        driver: DriverEntity?
    )

    suspend fun saveCustomerDetails(
        customerDetailRequest: CustomerDetailRequest?
    )

    suspend fun getCustomerDetails(): CustomerDetailsEntity?

    suspend fun getCurrentLogin(): LoginEntity?

    suspend fun getCustomerSession(): LoginWithCustomer?

    suspend fun getDriverSession(): LoginWithDriver?

    suspend fun logout()
}