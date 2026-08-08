package com.habitvault.core.domain.usecase

import com.habitvault.core.domain.model.CompletionStatus
import com.habitvault.core.domain.model.Habit
import com.habitvault.core.domain.model.Streak
import com.habitvault.core.domain.repository.CompletionRepository
import com.habitvault.core.domain.repository.HabitRepository
import com.habitvault.core.util.StreakCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetTodayHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val completionRepository: CompletionRepository,
) {
    operator fun invoke(today: LocalDate = LocalDate.now()): Flow<List<TodayHabit>> {
        return combine(
            habitRepository.getAllHabits(),
            completionRepository.getCompletionsForDate(today)
        ) { habits, todayCompletions ->
            habits.map { habit ->
                val isCompleted = todayCompletions.any {
                    it.habitId == habit.id && it.status == CompletionStatus.COMPLETED
                }
                val streak = StreakCalculator.calculate(
                    completions = todayCompletions.filter { it.habitId == habit.id },
                    frequency = habit.frequency,
                    today = today
                )
                TodayHabit(habit = habit, isCompleted = isCompleted, streak = streak)
            }
        }
    }
}

data class TodayHabit(
    val habit: Habit,
    val isCompleted: Boolean,
    val streak: Streak,
)
