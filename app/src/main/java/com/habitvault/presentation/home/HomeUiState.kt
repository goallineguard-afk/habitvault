package com.habitvault.presentation.home

import com.habitvault.core.domain.usecase.TodayHabit

data class HomeUiState(
    val habits: List<TodayHabit> = emptyList(),
    val completionPercentage: Int = 0,
    val totalCurrentStreak: Int = 0,
    val totalLongestStreak: Int = 0,
    val totalCompleted: Int = 0,
    val newHabitName: String = "",
    val isLoading: Boolean = false,
    val isDarkTheme: Boolean = false,
)
