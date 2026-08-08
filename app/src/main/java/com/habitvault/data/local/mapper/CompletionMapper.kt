package com.habitvault.data.local.mapper

import com.habitvault.core.domain.model.Completion
import com.habitvault.core.domain.model.CompletionStatus
import com.habitvault.data.local.db.entity.CompletionEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object CompletionMapper {
    fun toDomain(entity: CompletionEntity): Completion {
        return Completion(
            id = entity.id,
            habitId = entity.habitId,
            date = LocalDate.parse(entity.date),
            status = CompletionStatus.valueOf(entity.status),
            timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(entity.timestamp), ZoneId.systemDefault()
            ),
            note = entity.note
        )
    }

    fun toEntity(domain: Completion): CompletionEntity {
        return CompletionEntity(
            id = domain.id,
            habitId = domain.habitId,
            date = domain.date.toString(),
            status = domain.status.name,
            timestamp = domain.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            note = domain.note
        )
    }
}
