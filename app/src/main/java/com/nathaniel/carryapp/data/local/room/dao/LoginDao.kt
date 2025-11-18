package com.nathaniel.carryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.data.local.room.relations.LoginWithCustomer
import com.nathaniel.carryapp.data.local.room.relations.LoginWithDriver

@Dao
interface LoginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogin(login: LoginEntity)

    @Query("SELECT * FROM login_table WHERE userId = :userId LIMIT 1")
    suspend fun getLogin(userId: Long): LoginEntity?

    @Query("SELECT * FROM login_table LIMIT 1")
    suspend fun getCurrentLogin(): LoginEntity?

    @Query("DELETE FROM login_table")
    suspend fun clearLogin()

    // ------- RELATIONS -------
    @Transaction
    @Query("SELECT * FROM login_table WHERE customerId IS NOT NULL LIMIT 1")
    suspend fun getLoginWithCustomer(): LoginWithCustomer?

    @Transaction
    @Query("SELECT * FROM login_table WHERE driverId IS NOT NULL LIMIT 1")
    suspend fun getLoginWithDriver(): LoginWithDriver?
}