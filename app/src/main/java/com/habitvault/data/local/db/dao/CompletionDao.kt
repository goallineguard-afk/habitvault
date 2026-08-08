package com.habitvault.data.local.db.dao

import androidx.room.*
import com.habitvault.data.local.db.entity.CompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletionDao {
    @Query("SELECT * FROM completions WHERE habit_id = :habitId ORDER BY date DESC")
    fun getByHabitId(habitId: String): Flow<List<CompletionEntity>>

    @Query("SELECT * FROM completions WHERE habit_id = :habitId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getByHabitIdInRange(habitId: String, startDate: String, endDate: String): Flow<List<CompletionEntity>>

    @Query("SELECT * FROM completions WHERE habit_id = :habitId AND date = :date LIMIT 1")
    suspend fun getByHabitAndDate(habitId: String, date: String): CompletionEntity?

    @Query("SELECT * FROM completions WHERE date = :date")
    fun getByDate(date: String): Flow<List<CompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(completion: CompletionEntity)

    @Query("DELETE FROM completions WHERE habit_id = :habitId")
    suspend fun deleteByHabitId(habitId: String)

    @Query("SELECT date FROM completions WHERE habit_id = :habitId AND status = 'COMPLETED' ORDER BY date DESC LIMIT 1")
    suspend fun getLastCompletionDate(habitId: String): String?

    @Query("SELECT COUNT(*) FROM completions WHERE habit_id = :habitId AND status = 'COMPLETED'")
    suspend fun getCompletionCount(habitId: String): Int
}
