@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.jedi.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.jedi.R
import com.senijoshua.jedi.ui.model.Jedi

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
        },
        onErrorMessageShown = {
            viewModel.errorMessageShown()
        }
    )

}

@Composable
private fun JediListContent(
    modifier: Modifier = Modifier,
    uiState: JediListScreenUiState,
    onNavigateToJediDetail: (jediId: String) -> Unit,
    onErrorMessageShown: () -> Unit
) {
    // Hoist the (snack bar host) state outside the Scaffold and store it in the composition.
    val snackbarHostState = remember { SnackbarHostState() }

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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Screen layout
            if (uiState.isLoadingJedis) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            LazyColumn {
                items(uiState.jedis) {
                    JediItem(jedi = it, onJediClicked = { jediId ->
                        onNavigateToJediDetail(jediId)
                    })
                }
            }

            uiState.errorMessage?.let { message ->
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message = message)
                    onErrorMessageShown()
                }
            }
        }

    }
}

@Composable
private fun JediItem(
    jedi: Jedi,
    onJediClicked: (String) -> Unit
) {
    // TODO Use a Row for the list item
    //  Add a preview composable for the list item and the overall screen with fake data
}
