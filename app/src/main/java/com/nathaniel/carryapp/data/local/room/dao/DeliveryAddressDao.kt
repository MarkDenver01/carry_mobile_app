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
}
