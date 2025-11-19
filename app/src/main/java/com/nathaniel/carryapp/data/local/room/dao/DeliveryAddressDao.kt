package com.nathaniel.carryapp.data.local.room.dao

import androidx.room.*
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity

@Dao
interface DeliveryAddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAddress(address: DeliveryAddressEntity)

    @Query("SELECT * FROM delivery_address LIMIT 1")
    suspend fun getAddress(): DeliveryAddressEntity?

    @Query("DELETE FROM delivery_address")
    suspend fun clearAddress()

    @Query("SELECT COUNT(*) FROM delivery_address")
    suspend fun getAddressCount(): Int

    @Query("""
        UPDATE delivery_address 
        SET 
            provinceName = :provinceName,
            cityName = :cityName,
            barangayName = :barangayName,
            addressDetail = :addressDetail
        WHERE id = 1
    """)
    suspend fun updateAddressFields(
        provinceName: String?,
        cityName: String?,
        barangayName: String?,
        addressDetail: String?
    )
}
