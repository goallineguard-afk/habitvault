package com.habitvault.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.habitvault.presentation.common.components.HabitCard
import com.habitvault.presentation.common.components.ProgressRing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HabitVault") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ProgressRing(percentage = uiState.completionPercentage)
                }
            }

            item {
                OutlinedTextField(
                    value = uiState.newHabitName,
                    onValueChange = { viewModel.onEvent(HomeUiEvent.OnHabitNameChange(it)) },
                    label = { Text("New habit...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.onEvent(HomeUiEvent.OnAddHabitClick)
                    }),
                    trailingIcon = {
                        TextButton(onClick = { viewModel.onEvent(HomeUiEvent.OnAddHabitClick) }) {
                            Text("Add")
                        }
                    }
                )
            }

            items(uiState.habits) { todayHabit ->
                HabitCard(
                    name = todayHabit.habit.name,
                    isCompleted = todayHabit.isCompleted,
                    streak = todayHabit.streak.currentStreak,
                    color = todayHabit.habit.color,
                    onToggle = { viewModel.onEvent(HomeUiEvent.OnToggleHabit(todayHabit.habit.id)) }
                )
            }

            if (uiState.habits.isEmpty()) {
                item { EmptyHabitsState() }
            }
        }
    }
}

@Composable
private fun EmptyHabitsState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No habits yet", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Add your first habit above.", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
