package com.senijoshua.jedi.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * App-level stateholder that manages app-level UI logic and state
 * for the app-level composable; in this case, an app-level controlled
 * navigation paradigm.
 */
class JediAppState(
    val navController: NavHostController
)

/**
 * Creates the [JediAppState] and stores it in the Composition
 * during initial composition so as to persist it across recomposition.
 */
@Composable
fun rememberJediAppState(
    navController: NavHostController = rememberNavController(),
) = remember(navController) {
    JediAppState(navController)
}
