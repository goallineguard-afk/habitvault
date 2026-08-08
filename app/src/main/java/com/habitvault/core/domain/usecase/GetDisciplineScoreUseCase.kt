package com.habitvault.core.domain.usecase

import com.habitvault.core.domain.model.DisciplineScore
import com.habitvault.core.domain.repository.CompletionRepository
import com.habitvault.core.domain.repository.HabitRepository
import com.habitvault.core.util.DisciplineScoreEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class GetDisciplineScoreUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val completionRepository: CompletionRepository,
) {
    operator fun invoke(
        today: LocalDate = LocalDate.now(),
        windowDays: Int = 30
    ): Flow<DisciplineScore> {
        return combine(
            habitRepository.getAllHabits(),
            completionRepository.getCompletionsForDate(today)
        ) { habits, completions ->
            DisciplineScoreEngine.calculate(
                habits = habits,
                completions = completions,
                today = today,
                windowDays = windowDays
            )
        }
    }
}
