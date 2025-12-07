package com.nathaniel.carryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathaniel.carryapp.data.local.room.entity.ReorderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReorderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ReorderEntity>)

    @Query("SELECT * FROM reorder_history_table ORDER BY orderedAt DESC")
    fun getAllHistory(): Flow<List<ReorderEntity>>

    @Query("DELETE FROM reorder_history_table")
    suspend fun clearAll()
}