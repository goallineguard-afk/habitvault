package com.habitvault.data.repository

import com.habitvault.core.domain.model.Goal
import com.habitvault.core.domain.repository.GoalRepository
import com.habitvault.data.local.db.dao.GoalDao
import com.habitvault.data.local.mapper.GoalMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getActiveGoals(): Flow<List<Goal>> =
        goalDao.getActive().map { it.map(GoalMapper::toDomain) }

    override fun getGoalsForHabit(habitId: String): Flow<List<Goal>> =
        goalDao.getByHabitId(habitId).map { it.map(GoalMapper::toDomain) }

    override suspend fun getGoalById(id: String): Goal? =
        goalDao.getById(id)?.let(GoalMapper::toDomain)

    override suspend fun insertGoal(goal: Goal): String {
        goalDao.insert(GoalMapper.toEntity(goal))
        return goal.id
    }

    override suspend fun markGoalAchieved(id: String) {
        goalDao.markAchieved(id, System.currentTimeMillis())
    }

    override suspend fun deleteGoal(id: String) {
        goalDao.delete(id)
    }
}
