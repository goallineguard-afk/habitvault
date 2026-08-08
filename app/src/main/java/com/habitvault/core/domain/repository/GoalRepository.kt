package com.habitvault.core.domain.repository

import com.habitvault.core.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getActiveGoals(): Flow<List<Goal>>
    fun getGoalsForHabit(habitId: String): Flow<List<Goal>>
    suspend fun getGoalById(id: String): Goal?
    suspend fun insertGoal(goal: Goal): String
    suspend fun markGoalAchieved(id: String)
    suspend fun deleteGoal(id: String)
}
