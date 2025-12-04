package com.nathaniel.carryapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nathaniel.carryapp.data.local.room.dao.AgreementTermsDao
import com.nathaniel.carryapp.data.local.room.dao.CartDao
import com.nathaniel.carryapp.data.local.room.dao.CustomerDao
import com.nathaniel.carryapp.data.local.room.dao.DeliveryAddressDao
import com.nathaniel.carryapp.data.local.room.dao.DriverDao
import com.nathaniel.carryapp.data.local.room.dao.LoginDao
import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity
import com.nathaniel.carryapp.data.local.room.entity.CartItemEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginSessionEntity

@Database(
    entities = [
        LoginSessionEntity::class,
        LoginEntity::class,
        CustomerEntity::class,
        DriverEntity::class,
        DeliveryAddressEntity::class,
        CustomerDetailsEntity::class,
        CartItemEntity::class,
        AgreementTermsEntity::class
    ],
    version = 7,
    exportSchema = true
)
abstract class CarryDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao
    abstract fun customerDao(): CustomerDao
    abstract fun driverDao(): DriverDao
    abstract fun deliveryAddressDao(): DeliveryAddressDao
    abstract fun cartDao(): CartDao
    abstract fun agreementTermsDao(): AgreementTermsDao
}