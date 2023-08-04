package com.senijoshua.jedi.ui.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val JediListRoute = "list"

/**
 * NavGraph for the JediListScreen which provides runtime type safety when using Navigation Compose.
 * See: https://developer.android.com/guide/navigation/design/type-safety
 *
 * The nav graph can be nested into the root graph in the NavHost.
 */
fun NavGraphBuilder.jediListScreen(
    onNavigateToJediDetail: (jediId: String) -> Unit
) {
    composable(JediListRoute){
        val viewModel = hiltViewModel<JediListViewModel>()
        JediListScreen(viewModel = viewModel, onNavigateToJediDetail = {
            // handle event exposed by the screen
            // trigger the navigation event exposed to the caller of this graph for handling.
            onNavigateToJediDetail(it)
        })
    }
}
