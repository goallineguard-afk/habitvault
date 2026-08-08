package com.habitvault.core.domain.repository

import com.habitvault.core.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    fun getArchivedHabits(): Flow<List<Habit>>
    suspend fun getHabitById(id: String): Habit?
    suspend fun insertHabit(habit: Habit): String
    suspend fun updateHabit(habit: Habit)
    suspend fun archiveHabit(id: String)
    suspend fun deleteHabit(id: String)
    suspend fun reorderHabits(orderedIds: List<String>)
    suspend fun getNextOrderIndex(): Int
}
