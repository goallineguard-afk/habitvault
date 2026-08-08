package com.habitvault.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.habitvault.presentation.goals.GoalsScreen
import com.habitvault.presentation.home.HomeScreen
import com.habitvault.presentation.journal.JournalScreen
import com.habitvault.presentation.stats.StatsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Journal.route) { JournalScreen() }
        composable(Screen.Goals.route) { GoalsScreen() }
        composable(Screen.Stats.route) { StatsScreen() }
    }
}

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Home")
    data object Journal : Screen("journal", "Journal")
    data object Goals : Screen("goals", "Goals")
    data object Stats : Screen("stats", "Stats")
}
