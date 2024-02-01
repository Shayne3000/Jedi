package com.senijoshua.jedi.ui.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val JEDI_DETAIL_ID_ARG = "jediId"
private const val JEDI_DETAIL_NAME_ARG = "jediName"

private const val JEDI_DETAIL_ROUTE =
    "detail/{$JEDI_DETAIL_ID_ARG}?$JEDI_DETAIL_NAME_ARG={$JEDI_DETAIL_NAME_ARG}"

/**
 * Extension function to allow other destinations to navigate to the detail screen. Ideally, each
 * screen should have this.
 *
 * This helps to foster type safety as you specifically define what route gets used for navigation
 * and you do not allow callers to just specify any value as the route to which to navigate.
 */
fun NavController.navigateToDetailScreen(jediId: Int, jediName: String) {
    // navigation compose uses a web-esque url route pattern for navigation.
    this.navigate("detail/$jediId?$JEDI_DETAIL_NAME_ARG=$jediName")
}

/**
 * NavGraph for the JediDetailScreen for type safety. It decouples the
 * stateless screen-level JediDetailScreen composable function from
 * Navigation-specific logic and serves as the bridge between them.
 *
 * The graph will be nested into the NavHost.
 */
fun NavGraphBuilder.jediDetailScreen(
    onBackClicked: () -> Unit
) {
    composable(
        JEDI_DETAIL_ROUTE,
        arguments = listOf(navArgument(JEDI_DETAIL_NAME_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val jediDetailViewModel = hiltViewModel<JediDetailViewModel>()
        // Extract the jediName argument here since it is needed in the TopAppBar,
        // and also retrieve the jediId argument in the ViewModel as it is needed for the DB lookup.
        JediDetailScreen(
            topBarTitle = backStackEntry.arguments?.getString(JEDI_DETAIL_NAME_ARG)!!,
            viewModel = jediDetailViewModel,
            onBackClicked = {
                onBackClicked()
            })
    }
}
