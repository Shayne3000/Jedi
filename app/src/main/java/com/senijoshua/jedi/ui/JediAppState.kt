package com.senijoshua.jedi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * App-level stateholder that manages app-level UI logic and state for the app-level composable.
 */
class JediAppState(
    val navController: NavHostController
) {
}

/**
 * Creates the [JediAppState] and stores it in the Composition during initial Composition
 * Read more in ui state handling
 */
@Composable
fun rememberJediAppState(
    navController: NavHostController = rememberNavController(),
) = remember(navController) {
    JediAppState(navController)
}
