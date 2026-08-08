package com.habitvault.data.repository

import com.habitvault.core.domain.model.Completion
import com.habitvault.core.domain.model.CompletionStatus
import com.habitvault.core.domain.repository.CompletionRepository
import com.habitvault.data.local.db.dao.CompletionDao
import com.habitvault.data.local.mapper.CompletionMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompletionRepositoryImpl @Inject constructor(
    private val completionDao: CompletionDao
) : CompletionRepository {

    override fun getCompletionsForHabit(habitId: String): Flow<List<Completion>> =
        completionDao.getByHabitId(habitId).map { it.map(CompletionMapper::toDomain) }

    override fun getCompletionsForHabitInRange(
        habitId: String, startDate: LocalDate, endDate: LocalDate
    ): Flow<List<Completion>> =
        completionDao.getByHabitIdInRange(habitId, startDate.toString(), endDate.toString())
            .map { it.map(CompletionMapper::toDomain) }

    override suspend fun getCompletionStatus(habitId: String, date: LocalDate): CompletionStatus? =
        completionDao.getByHabitAndDate(habitId, date.toString())?.let {
            CompletionStatus.valueOf(it.status)
        }

    override fun getCompletionsForDate(date: LocalDate): Flow<List<Completion>> =
        completionDao.getByDate(date.toString()).map { it.map(CompletionMapper::toDomain) }

    override suspend fun insertCompletion(completion: Completion) {
        completionDao.insert(CompletionMapper.toEntity(completion))
    }

    override suspend fun toggleCompletion(habitId: String, date: LocalDate): CompletionStatus {
        val existing = completionDao.getByHabitAndDate(habitId, date.toString())
        return if (existing != null) {
            val newStatus = if (existing.status == CompletionStatus.COMPLETED.name)
                CompletionStatus.MISSED else CompletionStatus.COMPLETED
            val updated = existing.copy(status = newStatus.name)
            completionDao.insert(updated)
            CompletionStatus.valueOf(newStatus.name)
        } else {
            val completion = Completion(
                habitId = habitId,
                date = date,
                status = CompletionStatus.COMPLETED
            )
            completionDao.insert(CompletionMapper.toEntity(completion))
            CompletionStatus.COMPLETED
        }
    }

    override suspend fun getLastCompletionDate(habitId: String): LocalDate? =
        completionDao.getLastCompletionDate(habitId)?.let { LocalDate.parse(it) }

    override suspend fun getCompletionCount(habitId: String): Int =
        completionDao.getCompletionCount(habitId)

    override suspend fun deleteCompletionsForHabit(habitId: String) {
        completionDao.deleteByHabitId(habitId)
    }
}
