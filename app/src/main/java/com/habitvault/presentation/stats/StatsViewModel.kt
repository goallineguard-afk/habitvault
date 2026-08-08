package com.habitvault.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitvault.core.domain.model.DisciplineScore
import com.habitvault.core.domain.usecase.GetDisciplineScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getDisciplineScoreUseCase: GetDisciplineScoreUseCase
) : ViewModel() {

    private val _exportEvent = MutableSharedFlow<String>()
    val exportEvent: SharedFlow<String> = _exportEvent.asSharedFlow()

    val uiState: StateFlow<StatsUiState> = getDisciplineScoreUseCase()
        .map { score ->
            StatsUiState(
                disciplineScore = score,
                weeklyStats = WeeklyStatsUiModel(
                    completedCount = (score.overall * 0.7).toInt(),
                    missedCount = (score.overall * 0.3).toInt(),
                    completionRate = score.overall
                ),
                monthlyStats = MonthlyStatsUiModel(
                    completedCount = score.overall * 4,
                    bestStreak = score.overall / 10,
                    avgDisciplineScore = score.overall
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StatsUiState()
        )

    fun onEvent(event: StatsUiEvent) {
        when (event) {
            is StatsUiEvent.OnExportData -> exportData()
        }
    }

    private fun exportData() {
        viewModelScope.launch {
            _exportEvent.emit(
                """{"exportedAt":"${java.time.LocalDateTime.now()}","app":"HabitVault","version":"1.0.0","data":{"habits":[],"completions":[],"journal":[],"goals":[]}}"""
            )
        }
    }
}

data class StatsUiState(
    val disciplineScore: DisciplineScore? = null,
    val weeklyStats: WeeklyStatsUiModel = WeeklyStatsUiModel(),
    val monthlyStats: MonthlyStatsUiModel = MonthlyStatsUiModel()
)

data class WeeklyStatsUiModel(
    val completedCount: Int = 0,
    val missedCount: Int = 0,
    val completionRate: Int = 0
)

data class MonthlyStatsUiModel(
    val completedCount: Int = 0,
    val bestStreak: Int = 0,
    val avgDisciplineScore: Int = 0
)

sealed class StatsUiEvent {
    data object OnExportData : StatsUiEvent()
}
