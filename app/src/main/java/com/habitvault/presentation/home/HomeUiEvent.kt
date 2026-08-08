package com.habitvault.presentation.home

sealed class HomeUiEvent {
    data class OnHabitNameChange(val name: String) : HomeUiEvent()
    data object OnAddHabitClick : HomeUiEvent()
    data class OnToggleHabit(val habitId: String) : HomeUiEvent()
    data class OnDeleteHabit(val habitId: String) : HomeUiEvent()
    data object OnThemeToggle : HomeUiEvent()
    data object OnRefresh : HomeUiEvent()
}
