package com.nathaniel.carryapp.data.local.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity

data class LoginWithCustomer(
    @Embedded val login: LoginEntity,

    @Relation(
        parentColumn = "customerId",
        entityColumn = "customerId"
    )
    val customer: CustomerEntity?
)

data class LoginWithDriver(
    @Embedded val login: LoginEntity,

    @Relation(
        parentColumn = "driverId",
        entityColumn = "driverId"
    )
    val driver: DriverEntity?
)