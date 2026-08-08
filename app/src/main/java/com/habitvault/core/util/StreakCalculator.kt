package com.habitvault.core.util

import com.habitvault.core.domain.model.Completion
import com.habitvault.core.domain.model.CompletionStatus
import com.habitvault.core.domain.model.Frequency
import com.habitvault.core.domain.model.Streak
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object StreakCalculator {
    fun calculate(
        completions: List<Completion>,
        frequency: Frequency,
        today: LocalDate = LocalDate.now()
    ): Streak {
        val completedDates = completions
            .filter { it.status == CompletionStatus.COMPLETED }
            .map { it.date }
            .distinct()
            .sortedDescending()

        if (completedDates.isEmpty()) {
            return Streak.ZERO
        }

        val currentStreak = calculateCurrentStreak(completedDates, frequency, today)
        val longestStreak = calculateLongestStreak(completedDates, frequency)
        val isActive = isStreakActive(completedDates, frequency, today)
        val lastCompleted = completedDates.firstOrNull()

        return Streak(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            isActive = isActive,
            lastCompletedDate = lastCompleted
        )
    }

    private fun calculateCurrentStreak(
        completedDates: List<LocalDate>,
        frequency: Frequency,
        today: LocalDate
    ): Int {
        if (completedDates.isEmpty()) return 0
        val mostRecent = completedDates.first()
        val daysSinceLastCompletion = ChronoUnit.DAYS.between(mostRecent, today)

        val maxAllowedGap = when (frequency) {
            is Frequency.Daily -> 1L
            is Frequency.Weekly -> 2L
            is Frequency.SpecificDays -> 2L
            is Frequency.Interval -> frequency.everyNDays.toLong()
        }

        if (daysSinceLastCompletion > maxAllowedGap) return 0

        var streak = 0
        var checkDate = today
        val completedSet = completedDates.toSet()

        while (true) {
            if (completedSet.contains(checkDate)) {
                streak++
                checkDate = checkDate.minusDays(1)
            } else if (!frequency.isExpectedOn(checkDate)) {
                checkDate = checkDate.minusDays(1)
            } else {
                break
            }
            if (ChronoUnit.DAYS.between(checkDate, today) > 365) break
        }
        return streak
    }

    private fun calculateLongestStreak(
        completedDates: List<LocalDate>,
        frequency: Frequency
    ): Int {
        if (completedDates.isEmpty()) return 0
        val sorted = completedDates.sorted()
        var longest = 0
        var current = 1

        for (i in 1 until sorted.size) {
            val prev = sorted[i - 1]
            val curr = sorted[i]
            val gap = ChronoUnit.DAYS.between(prev, curr)
            val maxGap = when (frequency) {
                is Frequency.Daily -> 1L
                is Frequency.Weekly -> 2L
                is Frequency.SpecificDays -> 2L
                is Frequency.Interval -> frequency.everyNDays.toLong()
            }
            if (gap <= maxGap) {
                current++
            } else {
                longest = maxOf(longest, current)
                current = 1
            }
        }
        return maxOf(longest, current)
    }

    private fun isStreakActive(
        completedDates: List<LocalDate>,
        frequency: Frequency,
        today: LocalDate
    ): Boolean {
        if (completedDates.isEmpty()) return false
        val mostRecent = completedDates.first()
        val daysSince = ChronoUnit.DAYS.between(mostRecent, today)
        return when (frequency) {
            is Frequency.Daily -> daysSince <= 1
            is Frequency.Weekly -> daysSince <= 2
            is Frequency.SpecificDays -> daysSince <= 2
            is Frequency.Interval -> daysSince <= frequency.everyNDays
        }
    }
}
