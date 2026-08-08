package com.habitvault.data.repository

import com.habitvault.core.domain.model.JournalEntry
import com.habitvault.core.domain.repository.JournalRepository
import com.habitvault.data.local.db.dao.JournalDao
import com.habitvault.data.local.mapper.JournalMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepositoryImpl @Inject constructor(
    private val journalDao: JournalDao
) : JournalRepository {

    override fun getAllEntries(): Flow<List<JournalEntry>> =
        journalDao.getAll().map { it.map(JournalMapper::toDomain) }

    override suspend fun getEntryForDate(date: LocalDate): JournalEntry? =
        journalDao.getByDate(date.toString())?.let(JournalMapper::toDomain)

    override fun getEntriesInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<JournalEntry>> =
        journalDao.getInRange(startDate.toString(), endDate.toString())
            .map { it.map(JournalMapper::toDomain) }

    override suspend fun saveEntry(entry: JournalEntry) {
        journalDao.insert(JournalMapper.toEntity(entry))
    }

    override suspend fun deleteEntry(id: String) {
        journalDao.delete(id)
    }
}
