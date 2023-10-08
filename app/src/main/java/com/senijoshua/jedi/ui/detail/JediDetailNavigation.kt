package com.senijoshua.jedi.ui.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val jediDetailRoute = "detail/{jediId}"

/**
 * Extension function to allow other destinations to navigate to the detail screen. Ideally, each
 * screen should have this.
 *
 * This helps to foster type safety as you specifically define what route gets used for navigation
 * and you do not allow callers to just specify any value as the route to which to navigate.
 */
fun NavController.navigateToDetailScreen(jediId: Int) {
    this.navigate("detail/$jediId")
}

/**
 * NavGraph for the JediDetailScreen for type safety. It decouples the
 * stateless screen-level JediDetailScreen composable function from
 * Navigation-specific logic and serves as the bridge between them.
 *
 * The graph will be nested into the NavHost.
 */
fun NavGraphBuilder.jediDetailScreen() {
    composable(jediDetailRoute) {
        val jediDetailViewModel = hiltViewModel<JediDetailViewModel>()
        JediDetailScreen(jediDetailViewModel)
    }
}
