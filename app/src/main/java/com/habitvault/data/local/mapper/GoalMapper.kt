package com.habitvault.data.local.mapper

import com.habitvault.core.domain.model.Goal
import com.habitvault.core.domain.model.TargetType
import com.habitvault.data.local.db.entity.GoalEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object GoalMapper {
    fun toDomain(entity: GoalEntity): Goal {
        return Goal(
            id = entity.id,
            habitId = entity.habitId,
            title = entity.title,
            targetType = TargetType.valueOf(entity.targetType),
            targetValue = entity.targetValue,
            deadline = entity.deadline?.let { LocalDate.parse(it) },
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(entity.createdAt), ZoneId.systemDefault()
            ),
            achievedAt = entity.achievedAt?.let {
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
            }
        )
    }

    fun toEntity(domain: Goal): GoalEntity {
        return GoalEntity(
            id = domain.id,
            habitId = domain.habitId,
            title = domain.title,
            targetType = domain.targetType.name,
            targetValue = domain.targetValue,
            deadline = domain.deadline?.toString(),
            createdAt = domain.createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            achievedAt = domain.achievedAt?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        )
    }
}
