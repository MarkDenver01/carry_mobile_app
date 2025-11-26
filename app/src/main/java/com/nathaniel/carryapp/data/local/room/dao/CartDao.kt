package com.nathaniel.carryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nathaniel.carryapp.data.local.room.entity.CartGroupEntity
import com.nathaniel.carryapp.data.local.room.entity.CartItemEntity

@Dao
interface CartDao {
    @Insert
    suspend fun insertItem(item: CartItemEntity)

    @Query(
        """
        DELETE FROM cart_items 
        WHERE id = (
            SELECT id FROM cart_items 
            WHERE productId = :productId 
            ORDER BY timestamp DESC 
            LIMIT 1
        )
    """
    )
    suspend fun deleteLatest(productId: Long)

    @Query("SELECT COUNT(*) FROM cart_items")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM cart_items WHERE productId = :productId")
    suspend fun getProductQty(productId: Long): Int

    @Query(
        """
        SELECT productId, COUNT(*) AS qty 
        FROM cart_items 
        GROUP BY productId
    """
    )
    suspend fun getGrouped(): List<CartGroupEntity>

    @Query("DELETE FROM cart_items")
    suspend fun clearAll()
}

