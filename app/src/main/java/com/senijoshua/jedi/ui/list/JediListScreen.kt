package com.senijoshua.jedi.ui.list

import androidx.compose.runtime.Composable

/**
 * Screen for the Jedi List that exposes an event to navigate to the detail screen
 * to the event handler i.e. caller.
 */
@Composable
fun JediListScreen(
    viewModel: JediListViewModel,
    onNavigateToJediDetail: (jediId: String) -> Unit
) {
    // TODO Add JediListUiState and expose an event to a handler update the List UI state.
    // TODO Add the viewmodel through hilt
    JediListContent()

}

@Composable
private fun JediListContent() {
    // TODO Add a Scaffold that holds the AppBar and the rest of the screen content i.e. JediList
}
