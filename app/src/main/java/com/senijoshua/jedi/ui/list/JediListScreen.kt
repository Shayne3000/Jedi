@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.jedi.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.jedi.R
import com.senijoshua.jedi.ui.util.JediPreview
import com.senijoshua.jedi.ui.model.Jedi
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
                    modifier = Modifier.width(dimensionResource(id = R.dimen.progress_indicator_width)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            LazyColumn {
                items(items = uiState.jedis, key = { jedi -> jedi.id }) {
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
    modifier: Modifier = Modifier,
    jedi: Jedi,
    onJediClicked: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(
                vertical = dimensionResource(id = R.dimen.list_item_card_vertical_padding),
                horizontal = dimensionResource(id = R.dimen.horizontal_padding)
            )
            .height(dimensionResource(id = R.dimen.list_item_height))
            .fillMaxWidth()
            .clickable { onJediClicked(jedi.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.list_item_vertical_padding),
                start = dimensionResource(
                    id = R.dimen.horizontal_padding
                )
            ),
            text = jedi.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Text(
            modifier = Modifier.padding(
                start = dimensionResource(
                    id = R.dimen.horizontal_padding
                )
            ),
            text = jedi.gender,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
    //  TODO Add a preview composable for the list item and the overall screen with fake data
}

@JediPreview
@Composable
fun JediItemPreview() {
    JediTheme {
        JediItem(jedi = Jedi("Luke_id", "Luke Skywalker", "Male"), onJediClicked = {})
    }
}

@Composable
fun JediListPreview() {
    JediTheme {

    }
}
