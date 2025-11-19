package com.nathaniel.carryapp.data.local.datasource

import com.nathaniel.carryapp.data.local.room.dao.DeliveryAddressDao
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.domain.datasource.AddressLocalDataSource
import jakarta.inject.Inject

class AddressLocalDataSourceImpl @Inject constructor(
    private val dao: DeliveryAddressDao
) : AddressLocalDataSource {

    override suspend fun save(address: DeliveryAddressEntity) {
        dao.saveAddress(address)
    }

    override suspend fun get(): DeliveryAddressEntity? {
        return dao.getAddress()
    }

    override suspend fun clear() {
        dao.clearAddress()
    }
}
