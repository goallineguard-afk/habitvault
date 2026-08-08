package com.habitvault.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class JournalEntry(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate,
    val content: String,
    val habitId: String? = null,
    val moodScore: Int? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    init {
        require(content.isNotBlank()) { "Journal content cannot be blank" }
        require(content.length <= 5000) { "Journal entry cannot exceed 5000 characters" }
        require(moodScore == null || moodScore in 1..10) { "Mood score must be 1-10" }
    }
}
