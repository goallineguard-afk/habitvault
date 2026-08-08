package com.habitvault.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Completion(
    val id: String = UUID.randomUUID().toString(),
    val habitId: String,
    val date: LocalDate,
    val status: CompletionStatus,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val note: String? = null,
) {
    init {
        require(habitId.isNotBlank()) { "Habit ID cannot be blank" }
        require(note == null || note.length <= 500) { "Note cannot exceed 500 characters" }
    }
}

enum class CompletionStatus {
    COMPLETED,
    MISSED,
    CANCELLED,
}
