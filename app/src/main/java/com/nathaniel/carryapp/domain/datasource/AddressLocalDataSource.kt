package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity

interface AddressLocalDataSource {
    suspend fun save(address: DeliveryAddressEntity)
    suspend fun get(): DeliveryAddressEntity?
    suspend fun clear()
}
