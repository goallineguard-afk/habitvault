package com.habitvault.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitvault.core.domain.model.Goal
import com.habitvault.core.domain.model.TargetType
import com.habitvault.core.domain.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : ViewModel() {

    val uiState: StateFlow<GoalsUiState> = goalRepository.getActiveGoals()
        .map { goals ->
            GoalsUiState(goals = goals.map { goal ->
                GoalUiModel(
                    id = goal.id, title = goal.title, targetType = goal.targetType,
                    targetValue = goal.targetValue, isAchieved = goal.isAchieved,
                    progress = if (goal.isAchieved) 1f else 0.5f
                )
            })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GoalsUiState()
        )

    fun onEvent(event: GoalsUiEvent) {
        when (event) {
            is GoalsUiEvent.OnAddGoal -> addGoal(event.title, event.targetType, event.targetValue)
            is GoalsUiEvent.OnMarkAchieved -> markAchieved(event.id)
            is GoalsUiEvent.OnDeleteGoal -> deleteGoal(event.id)
        }
    }

    private fun addGoal(title: String, targetType: TargetType, targetValue: Int) {
        viewModelScope.launch {
            goalRepository.insertGoal(Goal(habitId = "", title = title, targetType = targetType, targetValue = targetValue))
        }
    }

    private fun markAchieved(id: String) { viewModelScope.launch { goalRepository.markGoalAchieved(id) } }
    private fun deleteGoal(id: String) { viewModelScope.launch { goalRepository.deleteGoal(id) } }
}

data class GoalsUiState(val goals: List<GoalUiModel> = emptyList())
data class GoalUiModel(val id: String, val title: String, val targetType: TargetType, val targetValue: Int, val isAchieved: Boolean, val progress: Float)
sealed class GoalsUiEvent {
    data class OnAddGoal(val title: String, val targetType: TargetType, val targetValue: Int) : GoalsUiEvent()
    data class OnMarkAchieved(val id: String) : GoalsUiEvent()
    data class OnDeleteGoal(val id: String) : GoalsUiEvent()
}
