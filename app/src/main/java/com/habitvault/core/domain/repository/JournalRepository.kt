package com.habitvault.core.domain.repository

import com.habitvault.core.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface JournalRepository {
    fun getAllEntries(): Flow<List<JournalEntry>>
    suspend fun getEntryForDate(date: LocalDate): JournalEntry?
    fun getEntriesInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<JournalEntry>>
    suspend fun saveEntry(entry: JournalEntry)
    suspend fun deleteEntry(id: String)
}
