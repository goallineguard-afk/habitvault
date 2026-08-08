package com.habitvault.core.domain.repository

import com.habitvault.core.domain.model.Completion
import com.habitvault.core.domain.model.CompletionStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CompletionRepository {
    fun getCompletionsForHabit(habitId: String): Flow<List<Completion>>
    fun getCompletionsForHabitInRange(habitId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<Completion>>
    suspend fun getCompletionStatus(habitId: String, date: LocalDate): CompletionStatus?
    fun getCompletionsForDate(date: LocalDate): Flow<List<Completion>>
    suspend fun insertCompletion(completion: Completion)
    suspend fun toggleCompletion(habitId: String, date: LocalDate): CompletionStatus
    suspend fun getLastCompletionDate(habitId: String): LocalDate?
    suspend fun getCompletionCount(habitId: String): Int
    suspend fun deleteCompletionsForHabit(habitId: String)
}
