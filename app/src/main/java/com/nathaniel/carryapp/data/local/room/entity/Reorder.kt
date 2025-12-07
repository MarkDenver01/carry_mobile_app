package com.nathaniel.carryapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reorder_history_table")
data class ReorderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val productId: Long,
    val name: String,
    val imageUrl: String,
    val weight: String,
    val price: Double,
    val qty: Int,
    val categoryName: String,
    val expiryDate: String?,
    val inDate: String?,

    val orderedAt: Long = System.currentTimeMillis()
)