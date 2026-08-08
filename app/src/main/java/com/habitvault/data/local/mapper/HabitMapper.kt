package com.habitvault.data.local.mapper

import com.habitvault.core.domain.model.DayOfWeek
import com.habitvault.core.domain.model.Frequency
import com.habitvault.core.domain.model.Habit
import com.habitvault.core.domain.model.HabitColor
import com.habitvault.data.local.db.entity.HabitEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object HabitMapper {
    fun toDomain(entity: HabitEntity): Habit {
        return Habit(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            frequency = parseFrequency(entity.frequencyType, entity.frequencyValue),
            color = HabitColor.valueOf(entity.color),
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(entity.createdAt), ZoneId.systemDefault()
            ),
            archived = entity.archived,
            orderIndex = entity.orderIndex,
            reminderTime = entity.reminderTime
        )
    }

    fun toEntity(domain: Habit): HabitEntity {
        return HabitEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            frequencyType = serializeFrequencyType(domain.frequency),
            frequencyValue = serializeFrequencyValue(domain.frequency),
            color = domain.color.name,
            createdAt = domain.createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            archived = domain.archived,
            orderIndex = domain.orderIndex,
            reminderTime = domain.reminderTime
        )
    }

    private fun parseFrequency(type: String, value: String?): Frequency {
        return when (type) {
            "DAILY" -> Frequency.Daily
            "WEEKLY" -> {
                val parts = value?.split("|") ?: return Frequency.Weekly(1, emptySet())
                val times = parts[0].toIntOrNull() ?: 1
                val days = parts.getOrNull(1)?.split(",")?.mapNotNull {
                    runCatching { DayOfWeek.valueOf(it) }.getOrNull()
                }?.toSet() ?: emptySet()
                Frequency.Weekly(times, days)
            }
            "SPECIFIC_DAYS" -> {
                val days = value?.split(",")?.mapNotNull {
                    runCatching { DayOfWeek.valueOf(it) }.getOrNull()
                }?.toSet() ?: emptySet()
                Frequency.SpecificDays(days)
            }
            "INTERVAL" -> Frequency.Interval(value?.toIntOrNull() ?: 1)
            else -> Frequency.Daily
        }
    }

    private fun serializeFrequencyType(frequency: Frequency): String = when (frequency) {
        is Frequency.Daily -> "DAILY"
        is Frequency.Weekly -> "WEEKLY"
        is Frequency.SpecificDays -> "SPECIFIC_DAYS"
        is Frequency.Interval -> "INTERVAL"
    }

    private fun serializeFrequencyValue(frequency: Frequency): String? = when (frequency) {
        is Frequency.Daily -> null
        is Frequency.Weekly -> "${frequency.timesPerWeek}|${frequency.days.joinToString(",")}"
        is Frequency.SpecificDays -> frequency.days.joinToString(",")
        is Frequency.Interval -> frequency.everyNDays.toString()
    }
}
