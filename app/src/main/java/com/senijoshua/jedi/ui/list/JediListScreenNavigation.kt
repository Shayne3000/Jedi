package com.senijoshua.jedi.ui.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val JediListRoute = "list"

/**
 * NavGraph for the JediList Screen that exposes an event to navigate to the detail screen
 * to an event handler i.e. the caller, which in this case is the NavHost.
 */
fun NavGraphBuilder.jediListScreen(
    onNavigateToJediDetail: (Int, String) -> Unit
) {
    composable(JediListRoute){
        val viewModel = hiltViewModel<JediListViewModel>()
        JediListScreen(viewModel = viewModel, onNavigateToJediDetail = { jediId, jediName ->
            onNavigateToJediDetail(jediId, jediName)
        })
    }
}
