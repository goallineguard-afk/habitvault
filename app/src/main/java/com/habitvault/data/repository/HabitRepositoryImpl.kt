package com.habitvault.data.repository

import com.habitvault.core.domain.model.Habit
import com.habitvault.core.domain.repository.HabitRepository
import com.habitvault.data.local.db.dao.HabitDao
import com.habitvault.data.local.mapper.HabitMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAllActive().map { it.map(HabitMapper::toDomain) }

    override fun getArchivedHabits(): Flow<List<Habit>> =
        habitDao.getAllArchived().map { it.map(HabitMapper::toDomain) }

    override suspend fun getHabitById(id: String): Habit? =
        habitDao.getById(id)?.let(HabitMapper::toDomain)

    override suspend fun insertHabit(habit: Habit): String {
        habitDao.insert(HabitMapper.toEntity(habit))
        return habit.id
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.update(HabitMapper.toEntity(habit))
    }

    override suspend fun archiveHabit(id: String) {
        habitDao.archive(id)
    }

    override suspend fun deleteHabit(id: String) {
        habitDao.delete(id)
    }

    override suspend fun reorderHabits(orderedIds: List<String>) {
        orderedIds.forEachIndexed { index, id ->
            habitDao.updateOrder(id, index)
        }
    }

    override suspend fun getNextOrderIndex(): Int {
        return (habitDao.getMaxOrderIndex() ?: -1) + 1
    }
}
