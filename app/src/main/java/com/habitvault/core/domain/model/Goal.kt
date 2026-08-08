package com.habitvault.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Goal(
    val id: String = UUID.randomUUID().toString(),
    val habitId: String,
    val title: String,
    val targetType: TargetType,
    val targetValue: Int,
    val deadline: LocalDate? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val achievedAt: LocalDateTime? = null,
) {
    val isAchieved: Boolean get() = achievedAt != null
    val isExpired: Boolean get() = deadline?.isBefore(LocalDate.now()) ?: false

    init {
        require(title.isNotBlank()) { "Goal title cannot be blank" }
        require(targetValue > 0) { "Target value must be positive" }
    }
}

enum class TargetType {
    TOTAL_COMPLETIONS,
    STREAK_DAYS,
    DAYS_IN_PERIOD,
    DISCIPLINE_SCORE,
}
