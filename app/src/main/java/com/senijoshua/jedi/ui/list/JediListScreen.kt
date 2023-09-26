@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.jedi.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults.titleContentColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
    // Read screen UI state from the business logic state holder i.e. ViewModel in a lifecycle-aware manner through the StateFlow and convert to Compose State.
    val screenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    JediListContent(
        modifier = modifier,
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.jedi_list_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.fillMaxSize().padding(paddingValues)) {
            // Screen layout

            // TODO Add list of Jedi Item composables here with the lazyList state
        }

    }
}
