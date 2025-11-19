package com.nathaniel.carryapp.data.repository

import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.domain.datasource.AddressLocalDataSource
import com.nathaniel.carryapp.domain.datasource.LoginLocalDataSource
import com.nathaniel.carryapp.domain.mapper.CustomerDetailsMapper
import com.nathaniel.carryapp.domain.request.CustomerRequest
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val addressLocalDataSource: AddressLocalDataSource,
    private val loginLocalDataSource: LoginLocalDataSource,
    private val tokenManager: TokenManager
) {

    suspend fun saveCustomerDetails(customerRequest: CustomerRequest) {
        loginLocalDataSource.saveCustomerDetails(customerRequest)
    }

    suspend fun getCustomerDetails(): CustomerRequest? {
        val entity: CustomerDetailsEntity? = loginLocalDataSource.getCustomerDetails()
        return entity?.let { CustomerDetailsMapper.fromEntity(it) }
    }
    fun saveMobileOrEmail(mobileOrEmail: String) {
        tokenManager.saveMobileOrEmail(mobileOrEmail)
    }

    fun getMobileOrEmail(): String? = tokenManager.getMobileOrEmail()

    suspend fun saveAddress(address: DeliveryAddressEntity) {
        addressLocalDataSource.save(address)
    }

    suspend fun getAddress(): DeliveryAddressEntity? = addressLocalDataSource.get()

    suspend fun clearAddress() = addressLocalDataSource.clear()

    suspend fun updateAddress(
        provinceName: String,
        cityName: String,
        barangayName: String,
        addressDetail: String
    ) {
        addressLocalDataSource.updateAddressFields(
            provinceName,
            cityName,
            barangayName,
            addressDetail
        )
    }
}