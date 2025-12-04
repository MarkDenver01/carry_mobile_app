package com.nathaniel.carryapp.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "login_session_table")
data class LoginSessionEntity(
    @PrimaryKey val userId: Long = 1,
    val email: String? = "",
    val session: Boolean = false
)

@Entity(tableName = "login_table")
data class LoginEntity(
    @PrimaryKey val userId: Long, // <-- backend user ID (DO NOT AUTO GENERATE)
    val userName: String? = "",
    val jwtToken: String,
    val jwtIssueAt: String,
    val jwtExpirationTime: String,
    val role: String? = null,
    val loginStatus: String? = null,
    val customerId: Long? = null, // reference to customer table
    val driverId: Long? = null // reference to driver table
)

@Entity(tableName = "customer_table")
data class CustomerEntity(
    @PrimaryKey val customerId: Long, // backend customerId
    val userId: Long,                 // foreign key reference to user
    val userName: String? = null,
    val email: String? = null,
    val mobileNumber: String? = null,
    val roleState: String? = null,
    val photoUrl: String? = null,
    val address: String? = null,
    val createdDate: String? = null,
    val accountStatus: String? = null
)

@Entity(tableName = "customer_details_table")
data class CustomerDetailsEntity(
    @PrimaryKey val customerId: Long = 1,
    val userName: String? = null,
    val email: String? = null,
    val mobileNumber: String? = null,
    val photoUrl: String? = null,
    val address: String? = null
)

@Entity(tableName = "driver_table")
data class DriverEntity(
    @PrimaryKey val driverId: Long, // backend customerId
    val userId: Long,                  // MUST be added
    val userName: String? = null,
    val email: String? = null,
    val mobileNumber: String? = null,
    val roleState: String? = null,
    val photoUrl: String? = null,
    val address: String? = null,
    val driverLicenseNumber: String? = null,
    val frontIdUrl: String? = null,
    val backIdUrl: String? = null,
    val createdDate: String? = null,
    val accountStatus: String? = null
)

@Entity(tableName = "delivery_address")
data class DeliveryAddressEntity(
    @PrimaryKey
    val id: Int = 1,

    val provinceCode: String,
    val provinceName: String,

    val cityCode: String,
    val cityName: String,

    val barangayCode: String,
    val barangayName: String,

    val addressDetail: String,
    val landmark: String?,

    val latitude: Double,
    val longitude: Double
)