package com.habitvault.data.local.db.dao

import androidx.room.*
import com.habitvault.data.local.db.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAll(): Flow<List<JournalEntity>>

    @Query("SELECT * FROM journal_entries WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): JournalEntity?

    @Query("SELECT * FROM journal_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getInRange(startDate: String, endDate: String): Flow<List<JournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntity)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun delete(id: String)
}
