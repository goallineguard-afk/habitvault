package com.habitvault.core.util

import com.habitvault.core.domain.model.Completion
import com.habitvault.core.domain.model.CompletionStatus
import com.habitvault.core.domain.model.DisciplineScore
import com.habitvault.core.domain.model.DisciplineScore.Grade
import com.habitvault.core.domain.model.Habit
import com.habitvault.core.domain.model.HabitScore
import com.habitvault.core.domain.model.HabitScore.Trend
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.pow
import kotlin.math.roundToInt

object DisciplineScoreEngine {
    private const val PASSING_THRESHOLD = 70
    private const val WINDOW_DAYS_DEFAULT = 30

    fun calculate(
        habits: List<Habit>,
        completions: List<Completion>,
        today: LocalDate = LocalDate.now(),
        windowDays: Int = WINDOW_DAYS_DEFAULT
    ): DisciplineScore {
        if (habits.isEmpty()) {
            return DisciplineScore(
                overall = 0, grade = Grade.F,
                breakdown = emptyList(), isFailing = true, windowDays = windowDays
            )
        }

        val windowStart = today.minusDays(windowDays.toLong() - 1)
        val completionsByHabit = completions.groupBy { it.habitId }

        val habitScores = habits.map { habit ->
            val habitCompletions = completionsByHabit[habit.id] ?: emptyList()
            calculateHabitScore(habit, habitCompletions, windowStart, today, windowDays)
        }

        val overallScore = habitScores.map { it.score }.average().roundToInt()

        return DisciplineScore(
            overall = overallScore,
            grade = Grade.fromScore(overallScore),
            breakdown = habitScores,
            isFailing = overallScore < PASSING_THRESHOLD,
            windowDays = windowDays
        )
    }

    private fun calculateHabitScore(
        habit: Habit,
        completions: List<Completion>,
        windowStart: LocalDate,
        today: LocalDate,
        windowDays: Int
    ): HabitScore {
        val completedDates = completions
            .filter { it.status == CompletionStatus.COMPLETED }
            .map { it.date }
            .toSet()

        val missedDates = completions
            .filter { it.status == CompletionStatus.MISSED }
            .map { it.date }
            .toSet()

        var expectedDays = 0
        var actualCompletions = 0
        var missedCount = 0

        var date = windowStart
        while (!date.isAfter(today)) {
            if (habit.frequency.isExpectedOn(date)) {
                expectedDays++
                when {
                    completedDates.contains(date) -> actualCompletions++
                    missedDates.contains(date) -> missedCount++
                    date.isBefore(today) || date.isEqual(today) -> missedCount++
                }
            }
            date = date.plusDays(1)
        }

        if (expectedDays == 0) {
            return HabitScore(habitId = habit.id, score = 0.0, trend = Trend.STABLE,
                completionRate = 0.0, missedCount = 0)
        }

        val baseScore = (actualCompletions.toDouble() / expectedDays) * 100
        val recencyWeight = calculateRecencyWeight(completedDates, windowStart, today)
        val missPenalty = missedCount.toDouble().pow(2) * 2
        val rawScore = (baseScore * recencyWeight) - missPenalty
        val finalScore = rawScore.coerceIn(0.0, 100.0)
        val trend = calculateTrend(completions, today)
        val completionRate = (actualCompletions.toDouble() / expectedDays) * 100

        return HabitScore(
            habitId = habit.id, score = finalScore, trend = trend,
            completionRate = completionRate, missedCount = missedCount
        )
    }

    private fun calculateRecencyWeight(
        completedDates: Set<LocalDate>,
        windowStart: LocalDate,
        today: LocalDate
    ): Double {
        val last7Days = (0..6).map { today.minusDays(it.toLong()) }.toSet()
        val last14Days = (0..13).map { today.minusDays(it.toLong()) }.toSet()

        val recent7Count = completedDates.count { it in last7Days }
        val recent14Count = completedDates.count { it in last14Days && it !in last7Days }
        val olderCount = completedDates.count { it !in last14Days && !it.isBefore(windowStart) }

        val totalWeighted = recent7Count * 2.0 + recent14Count * 1.5 + olderCount * 1.0
        val totalCount = completedDates.size.coerceAtLeast(1)

        return 0.7 + (totalWeighted / totalCount) * 0.3
    }

    private fun calculateTrend(completions: List<Completion>, today: LocalDate): Trend {
        val completedDates = completions
            .filter { it.status == CompletionStatus.COMPLETED }
            .map { it.date }
            .toSet()

        val last7 = (0..6).map { today.minusDays(it.toLong()) }
        val prev7 = (7..13).map { today.minusDays(it.toLong()) }

        val last7Count = last7.count { it in completedDates }
        val prev7Count = prev7.count { it in completedDates }
        val diff = last7Count - prev7Count

        return when {
            diff >= 2 -> Trend.IMPROVING
            diff <= -2 -> Trend.DECLINING
            else -> Trend.STABLE
        }
    }
}
