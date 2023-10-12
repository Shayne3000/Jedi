package com.senijoshua.jedi.ui.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val JediListRoute = "list"

/**
 * NavGraph for the JediList Screen which provides runtime type safety
 * when using Navigation Compose.
 * See: https://developer.android.com/guide/navigation/design/type-safety
 *
 * The nav graph can be nested into the root graph in the NavHost.
 */
fun NavGraphBuilder.jediListScreen(
    onNavigateToJediDetail: (Int, String) -> Unit
) {
    composable(JediListRoute){
        val viewModel = hiltViewModel<JediListViewModel>()
        JediListScreen(viewModel = viewModel, onNavigateToJediDetail = { jediId, jediName ->
            // handle event exposed by the screen
            // trigger the navigation event exposed to the caller of this graph for handling.
            onNavigateToJediDetail(jediId, jediName)
        })
    }
}
