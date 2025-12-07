package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.data.local.room.entity.CartGroupEntity
import com.nathaniel.carryapp.data.local.room.entity.ReorderEntity
import kotlinx.coroutines.flow.Flow

interface CartDatasource {
    suspend fun addItem(productId: Long)
    suspend fun removeLatest(productId: Long)
    suspend fun getTotalCount(): Int
    suspend fun getGroupedItems(): List<CartGroupEntity>
    suspend fun getProductQty(productId: Long): Int
    suspend fun clearAll()
    suspend fun saveOrderHistory(items: List<ReorderEntity>)
    fun getOrderHistory(): Flow<List<ReorderEntity>>
}