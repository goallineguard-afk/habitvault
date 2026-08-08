package com.habitvault.data.local.mapper

import com.habitvault.core.domain.model.JournalEntry
import com.habitvault.data.local.db.entity.JournalEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object JournalMapper {
    fun toDomain(entity: JournalEntity): JournalEntry {
        return JournalEntry(
            id = entity.id,
            date = LocalDate.parse(entity.date),
            content = entity.content,
            habitId = entity.habitId,
            moodScore = entity.moodScore,
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(entity.createdAt), ZoneId.systemDefault()
            ),
            updatedAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(entity.updatedAt), ZoneId.systemDefault()
            )
        )
    }

    fun toEntity(domain: JournalEntry): JournalEntity {
        return JournalEntity(
            id = domain.id,
            date = domain.date.toString(),
            content = domain.content,
            habitId = domain.habitId,
            moodScore = domain.moodScore,
            createdAt = domain.createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            updatedAt = domain.updatedAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
}
