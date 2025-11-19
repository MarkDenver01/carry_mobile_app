package com.nathaniel.carryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathaniel.carryapp.data.local.room.entity.CustomerDetailsEntity
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerDetails(customerDetailsEntity: CustomerDetailsEntity)

    @Query("SELECT * FROM customer_details_table")
    suspend fun getCustomerDetails(): CustomerDetailsEntity?

    @Query("SELECT * FROM customer_table WHERE customerId = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): CustomerEntity?

    @Query("SELECT * FROM customer_table WHERE userId = :userId LIMIT 1")
    suspend fun getCustomerByUserId(userId: Long): CustomerEntity?

    @Query("DELETE FROM customer_table")
    suspend fun clearCustomers()
}