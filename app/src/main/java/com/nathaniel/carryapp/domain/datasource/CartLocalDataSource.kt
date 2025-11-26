package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.data.local.room.entity.CartGroupEntity

interface CartLocalDataSource {
    suspend fun addItem(productId: Long)
    suspend fun removeLatest(productId: Long)
    suspend fun getTotalCount(): Int
    suspend fun getGroupedItems(): List<CartGroupEntity>

    suspend fun getProductQty(productId: Long): Int

    suspend fun clearAll()

}