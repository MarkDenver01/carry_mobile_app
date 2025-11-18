package com.nathaniel.carryapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nathaniel.carryapp.data.local.room.dao.CustomerDao
import com.nathaniel.carryapp.data.local.room.dao.DriverDao
import com.nathaniel.carryapp.data.local.room.dao.LoginDao
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity

@Database(
    entities = [
        LoginEntity::class,
        CustomerEntity::class,
        DriverEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class CarryDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao
    abstract fun customerDao(): CustomerDao
    abstract fun driverDao(): DriverDao
}