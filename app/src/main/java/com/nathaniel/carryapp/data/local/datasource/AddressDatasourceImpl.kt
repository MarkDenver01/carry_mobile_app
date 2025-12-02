package com.nathaniel.carryapp.data.local.datasource

import com.nathaniel.carryapp.data.local.room.dao.DeliveryAddressDao
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.domain.datasource.AddressDatasource
import jakarta.inject.Inject

class AddressDatasourceImpl @Inject constructor(
    private val dao: DeliveryAddressDao
) : AddressDatasource {

    override suspend fun save(address: DeliveryAddressEntity) {
        dao.saveAddress(address)
    }

    override suspend fun get(): DeliveryAddressEntity? {
        return dao.getAddress()
    }

    override suspend fun clear() {
        dao.clearAddress()
    }

    override suspend fun updateAddressFields(
        provinceName: String?,
        cityName: String?,
        barangayName: String?,
        addressDetail: String?
    ) {
        dao.updateAddressFields(
            provinceName,
            cityName,
            barangayName,
            addressDetail
        )
    }
}
