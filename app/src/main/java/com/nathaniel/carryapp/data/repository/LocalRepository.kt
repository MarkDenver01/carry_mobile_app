package com.nathaniel.carryapp.data.repository

import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.domain.datasource.AddressLocalDataSource
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val addressLocalDataSource: AddressLocalDataSource
) {
    suspend fun saveAddress(address: DeliveryAddressEntity) {
        addressLocalDataSource.save(address)
    }

    suspend fun getAddress(): DeliveryAddressEntity? = addressLocalDataSource.get()

    suspend fun clearAddress() = addressLocalDataSource.clear()
}