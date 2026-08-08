package com.habitvault.core.domain.model

import java.time.LocalDate

data class Streak(
    val currentStreak: Int,
    val longestStreak: Int,
    val isActive: Boolean,
    val lastCompletedDate: LocalDate?,
) {
    companion object {
        val ZERO = Streak(
            currentStreak = 0,
            longestStreak = 0,
            isActive = false,
            lastCompletedDate = null,
        )
    }
}
