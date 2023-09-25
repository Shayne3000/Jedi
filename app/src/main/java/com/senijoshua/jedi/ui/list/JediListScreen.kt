package com.senijoshua.jedi.ui.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.jedi.R
import com.senijoshua.jedi.ui.theme.JediTheme

/**
 * Screen for the Jedi List that exposes an event to navigate to the detail screen
 * to the event handler i.e. caller.
 */
@Composable
fun JediListScreen(
    modifier: Modifier = Modifier,
    viewModel: JediListViewModel,
    onNavigateToJediDetail: (jediId: String) -> Unit
) {
    // TODO Get JediListUiState from viewmodel and expose an event to a handler i.e. ViewModel to update said UI state.
    // Read screen UI state from the business logic state holder i.e. ViewModel in a lifecycle-aware manner through the StateFlow and convert to Compose State.
    val screenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    JediListContent(
        uiState = screenUiState,
        onNavigateToJediDetail = {
            onNavigateToJediDetail(it)
        }
    )

}

@Composable
private fun JediListContent(
    modifier: Modifier = Modifier,
    uiState: JediListScreenUiState,
    onNavigateToJediDetail: (jediId: String) -> Unit
) {
    // TODO Add a Scaffold that holds the AppBar and the rest of the screen content i.e.
    // TODO review theming in Compose UI
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.jedi_list_title),
                    // color = MaterialTheme.colorScheme.
                )
            })
        }
    ) { paddingValues ->  
        
    }
}
