package com.habitvault.core.domain.usecase

import com.habitvault.core.domain.model.CompletionStatus
import com.habitvault.core.domain.repository.CompletionRepository
import java.time.LocalDate
import javax.inject.Inject

class ToggleHabitCompletionUseCase @Inject constructor(
    private val completionRepository: CompletionRepository,
) {
    suspend operator fun invoke(habitId: String, date: LocalDate = LocalDate.now()): CompletionStatus {
        return completionRepository.toggleCompletion(habitId, date)
    }
}
