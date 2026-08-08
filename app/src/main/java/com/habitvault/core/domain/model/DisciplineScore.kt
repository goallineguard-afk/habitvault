package com.habitvault.core.domain.model

data class DisciplineScore(
    val overall: Int,
    val grade: Grade,
    val breakdown: List<HabitScore>,
    val isFailing: Boolean,
    val windowDays: Int = 30,
) {
    enum class Grade {
        A, B, C, D, F;

        companion object {
            fun fromScore(score: Int): Grade = when (score) {
                in 90..100 -> A
                in 80..89 -> B
                in 70..79 -> C
                in 60..69 -> D
                else -> F
            }
        }
    }
}

data class HabitScore(
    val habitId: String,
    val score: Double,
    val trend: Trend,
    val completionRate: Double,
    val missedCount: Int,
) {
    enum class Trend {
        IMPROVING,
        STABLE,
        DECLINING,
    }
}
