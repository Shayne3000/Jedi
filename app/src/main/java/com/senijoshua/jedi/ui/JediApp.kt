package com.senijoshua.jedi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.senijoshua.jedi.ui.detail.jediDetailScreen
import com.senijoshua.jedi.ui.detail.navigateToDetailScreen
import com.senijoshua.jedi.ui.list.JediListRoute
import com.senijoshua.jedi.ui.list.jediListScreen

/**
 * App-level composable for app-level UI elements and UI logic.
 * It holds the NavHost into which composable screens are swapped in and out.
 */
@Composable
fun JediApp(
    appState: JediAppState = rememberJediAppState()
) {
    val navController = appState.navController
    NavHost(navController = navController, startDestination = JediListRoute) {
        jediListScreen { jediId, jediName ->
            navController.navigateToDetailScreen(jediId, jediName)
        }
        jediDetailScreen {
            navController.popBackStack()
        }
    }
}
