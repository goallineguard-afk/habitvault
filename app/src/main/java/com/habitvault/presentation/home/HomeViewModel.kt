package com.habitvault.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitvault.core.domain.model.Frequency
import com.habitvault.core.domain.model.Habit
import com.habitvault.core.domain.model.HabitColor
import com.habitvault.core.domain.repository.HabitRepository
import com.habitvault.core.domain.usecase.GetTodayHabitsUseCase
import com.habitvault.core.domain.usecase.ToggleHabitCompletionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayHabitsUseCase: GetTodayHabitsUseCase,
    private val toggleHabitCompletionUseCase: ToggleHabitCompletionUseCase,
    private val habitRepository: HabitRepository,
) : ViewModel() {

    private val _newHabitName = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        getTodayHabitsUseCase(),
        _newHabitName
    ) { habits, newHabitName ->
        val completedCount = habits.count { it.isCompleted }
        val percentage = if (habits.isNotEmpty()) (completedCount * 100 / habits.size) else 0

        HomeUiState(
            habits = habits,
            completionPercentage = percentage,
            totalCurrentStreak = habits.sumOf { it.streak.currentStreak },
            totalLongestStreak = habits.maxOfOrNull { it.streak.longestStreak } ?: 0,
            totalCompleted = completedCount,
            newHabitName = newHabitName,
            isLoading = false,
            isDarkTheme = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnHabitNameChange -> _newHabitName.value = event.name
            is HomeUiEvent.OnAddHabitClick -> addHabit()
            is HomeUiEvent.OnToggleHabit -> toggleHabit(event.habitId)
            is HomeUiEvent.OnDeleteHabit -> deleteHabit(event.habitId)
            is HomeUiEvent.OnThemeToggle -> { }
            is HomeUiEvent.OnRefresh -> { }
        }
    }

    private fun addHabit() {
        val name = _newHabitName.value.trim()
        if (name.isBlank()) return

        viewModelScope.launch {
            val habit = Habit(
                name = name,
                frequency = Frequency.Daily,
                color = HabitColor.MINT,
                orderIndex = habitRepository.getNextOrderIndex()
            )
            habitRepository.insertHabit(habit)
            _newHabitName.value = ""
        }
    }

    private fun toggleHabit(habitId: String) {
        viewModelScope.launch {
            toggleHabitCompletionUseCase(habitId)
        }
    }

    private fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habitId)
        }
    }
}
