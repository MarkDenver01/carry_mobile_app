package com.nathaniel.carryapp.data.local.datasource

import com.nathaniel.carryapp.data.local.room.dao.CartDao
import com.nathaniel.carryapp.data.local.room.dao.ReorderDao
import com.nathaniel.carryapp.data.local.room.entity.CartGroupEntity
import com.nathaniel.carryapp.data.local.room.entity.CartItemEntity
import com.nathaniel.carryapp.data.local.room.entity.ReorderEntity
import com.nathaniel.carryapp.domain.datasource.CartDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartDatasourceImpl @Inject constructor(
    private val cartDao: CartDao,
    private val reorderDao: ReorderDao,
) : CartDatasource {
    override suspend fun addItem(productId: Long) {
        cartDao.insertItem(CartItemEntity(productId = productId))
    }

    override suspend fun removeLatest(productId: Long) {
        cartDao.deleteLatest(productId)
    }

    override suspend fun getTotalCount(): Int {
        return cartDao.getTotalCount()
    }

    override suspend fun getGroupedItems(): List<CartGroupEntity> {
        return cartDao.getGrouped()
    }

    override suspend fun getProductQty(productId: Long): Int {
        return cartDao.getProductQty(productId)
    }

    override suspend fun clearAll() = cartDao.clearAll()

    override suspend fun saveOrderHistory(items: List<ReorderEntity>) {
        reorderDao.clearAll()
        reorderDao.insertAll(items)
    }

    override fun getOrderHistory(): Flow<List<ReorderEntity>> {
        return reorderDao.getAllHistory()
    }

    override suspend fun clearOrderHistory() {
        reorderDao.clearAll()
    }
}