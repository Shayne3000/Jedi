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
 * Extension function to allow other destinations to navigate to the detail screen.
 */
fun NavController.navigateToDetailScreen(jediId: Int, jediName: String) {
    this.navigate("detail/$jediId?$JEDI_DETAIL_NAME_ARG=$jediName")
}

/**
 * NavGraph for the JediDetailScreen.
 */
fun NavGraphBuilder.jediDetailScreen(
    onBackClicked: () -> Unit
) {
    composable(
        JEDI_DETAIL_ROUTE,
        arguments = listOf(navArgument(JEDI_DETAIL_NAME_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val jediDetailViewModel = hiltViewModel<JediDetailViewModel>()

        JediDetailScreen(
            topBarTitle = backStackEntry.arguments?.getString(JEDI_DETAIL_NAME_ARG)!!,
            viewModel = jediDetailViewModel,
            onBackClicked = {
                onBackClicked()
            })
    }
}
