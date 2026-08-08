package com.habitvault.data.local.db.dao

import androidx.room.*
import com.habitvault.data.local.db.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE archived = 0 ORDER BY order_index ASC")
    fun getAllActive(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE archived = 1 ORDER BY created_at DESC")
    fun getAllArchived(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Query("UPDATE habits SET archived = 1 WHERE id = :id")
    suspend fun archive(id: String)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE habits SET order_index = :orderIndex WHERE id = :id")
    suspend fun updateOrder(id: String, orderIndex: Int)

    @Query("SELECT MAX(order_index) FROM habits WHERE archived = 0")
    suspend fun getMaxOrderIndex(): Int?
}
