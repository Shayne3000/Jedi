package com.senijoshua.jedi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.senijoshua.jedi.ui.detail.jediDetailScreen
import com.senijoshua.jedi.ui.detail.navigateToDetailScreen
import com.senijoshua.jedi.ui.list.JediListRoute
import com.senijoshua.jedi.ui.list.jediListScreen

/**
 * App-level composable for app-level UI elements and UI logic.
 * It holds the global coroutineState, NavHost, and the Snackbar Host.
 */
@Composable
fun JediApp(
    appState: JediAppState = rememberJediAppState()
) {
    val navController = appState.navController
    NavHost(navController = navController, startDestination = JediListRoute) {
        // the nested graphs representing each destination in the NavHost
        jediListScreen { jediId ->
            // Handle event to navigate to the Jedi Detail screen.
            // This pattern also works for cross-module navigation through the app module.
            navController.navigateToDetailScreen(jediId)
        }
        jediDetailScreen()
    }
}