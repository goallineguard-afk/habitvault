package com.habitvault.presentation.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitvault.core.domain.model.JournalEntry
import com.habitvault.core.domain.repository.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val journalRepository: JournalRepository
) : ViewModel() {

    val uiState: StateFlow<JournalUiState> = journalRepository.getAllEntries()
        .map { entries ->
            JournalUiState(entries = entries.map { entry ->
                JournalEntryUiModel(
                    id = entry.id, date = entry.date,
                    content = entry.content, moodScore = entry.moodScore
                )
            })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = JournalUiState()
        )

    fun onEvent(event: JournalUiEvent) {
        when (event) {
            is JournalUiEvent.OnAddEntry -> addEntry(event.content, event.moodScore)
            is JournalUiEvent.OnDeleteEntry -> deleteEntry(event.id)
        }
    }

    private fun addEntry(content: String, moodScore: Int?) {
        viewModelScope.launch {
            val entry = JournalEntry(date = LocalDate.now(), content = content, moodScore = moodScore)
            journalRepository.saveEntry(entry)
        }
    }

    private fun deleteEntry(id: String) {
        viewModelScope.launch { journalRepository.deleteEntry(id) }
    }
}

data class JournalUiState(val entries: List<JournalEntryUiModel> = emptyList())
data class JournalEntryUiModel(val id: String, val date: java.time.LocalDate, val content: String, val moodScore: Int?)
sealed class JournalUiEvent {
    data class OnAddEntry(val content: String, val moodScore: Int?) : JournalUiEvent()
    data class OnDeleteEntry(val id: String) : JournalUiEvent()
}
