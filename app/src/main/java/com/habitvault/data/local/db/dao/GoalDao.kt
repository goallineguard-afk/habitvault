package com.habitvault.data.local.db.dao

import androidx.room.*
import com.habitvault.data.local.db.entity.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals WHERE achieved_at IS NULL AND (deadline IS NULL OR deadline >= date('now')) ORDER BY created_at DESC")
    fun getActive(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE habit_id = :habitId ORDER BY created_at DESC")
    fun getByHabitId(habitId: String): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): GoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity)

    @Query("UPDATE goals SET achieved_at = :achievedAt WHERE id = :id")
    suspend fun markAchieved(id: String, achievedAt: Long)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun delete(id: String)
}
