package com.nathaniel.carryapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agreement_table")
data class AgreementTermsEntity(
    @PrimaryKey val agreementId: Long = 1,
    val email: String? = null,
    val agreement: Boolean = false
)