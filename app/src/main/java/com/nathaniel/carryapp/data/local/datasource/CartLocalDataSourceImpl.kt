package com.nathaniel.carryapp.data.local.datasource

import com.nathaniel.carryapp.data.local.room.dao.CartDao
import com.nathaniel.carryapp.data.local.room.entity.CartGroupEntity
import com.nathaniel.carryapp.data.local.room.entity.CartItemEntity
import com.nathaniel.carryapp.domain.datasource.CartLocalDataSource
import javax.inject.Inject

class CartLocalDataSourceImpl @Inject constructor(
    private val cartDao: CartDao,
) : CartLocalDataSource {
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
}