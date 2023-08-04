package com.senijoshua.jedi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.senijoshua.jedi.ui.list.JediListRoute

/**
 * App-level composable for app-level UI elements present across the app.
 * It holds the global coroutineState, NavHost, and the Snackbar Host.
 */
@Composable
fun JediApp(
    appState: JediAppState = rememberJediAppState()
) {
    NavHost(navController = appState.navController, startDestination = JediListRoute) {

    }
}
