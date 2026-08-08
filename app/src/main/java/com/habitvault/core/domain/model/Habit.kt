package com.habitvault.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val frequency: Frequency,
    val color: HabitColor = HabitColor.MINT,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val archived: Boolean = false,
    val orderIndex: Int = 0,
    val reminderTime: String? = null,
) {
    init {
        require(name.isNotBlank()) { "Habit name cannot be blank" }
        require(name.length <= 100) { "Habit name cannot exceed 100 characters" }
    }
}

sealed class Frequency {
    data object Daily : Frequency()
    data class Weekly(val timesPerWeek: Int, val days: Set<DayOfWeek>) : Frequency() {
        init {
            require(timesPerWeek in 1..7) { "Times per week must be 1-7" }
            require(days.size == timesPerWeek) { "Selected days must match times per week" }
        }
    }
    data class SpecificDays(val days: Set<DayOfWeek>) : Frequency() {
        init {
            require(days.isNotEmpty()) { "At least one day must be selected" }
        }
    }
    data class Interval(val everyNDays: Int) : Frequency() {
        init {
            require(everyNDays >= 1) { "Interval must be at least 1 day" }
        }
    }

    fun isExpectedOn(date: LocalDate): Boolean = when (this) {
        is Daily -> true
        is Weekly -> date.dayOfWeek.toString() in days.map { it.toString() }
        is SpecificDays -> date.dayOfWeek.toString() in days.map { it.toString() }
        is Interval -> {
            val dayOfYear = date.dayOfYear
            dayOfYear % everyNDays == 0
        }
    }

    companion object {
        fun default(): Frequency = Daily
    }
}

enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

enum class HabitColor(val hex: String) {
    MINT("#34D399"),
    SLATE("#64748B"),
    AMBER("#F59E0B"),
    ROSE("#F43F5E"),
    SKY("#0EA5E9"),
    VIOLET("#8B5CF6"),
}
