package com.nathaniel.carryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity

@Dao
interface DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriver(driver: DriverEntity)

    @Query("SELECT * FROM driver_table WHERE driverId = :driverId LIMIT 1")
    suspend fun getDriverById(driverId: Long): DriverEntity?

    @Query("SELECT * FROM driver_table WHERE userId = :userId LIMIT 1")
    suspend fun getDriverByUserId(userId: Long): DriverEntity?

    @Query("DELETE FROM driver_table")
    suspend fun clearDrivers()
}