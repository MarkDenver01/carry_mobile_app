package com.nathaniel.carryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathaniel.carryapp.data.local.room.entity.AgreementTermsEntity

@Dao
interface AgreementTermsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgreement(item: AgreementTermsEntity)

    @Query("SELECT agreement FROM agreement_table WHERE email = :email LIMIT 1")
    suspend fun isVerified(email: String): Boolean?

    @Query("DELETE FROM agreement_table")
    suspend fun clearAgreementStatus()
}